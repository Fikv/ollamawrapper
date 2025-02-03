package com.wsiz.ollamawrapper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wsiz.grpc.AuthServiceGrpc;
import com.wsiz.grpc.AuthServiceOuterClass;
import com.wsiz.ollamawrapper.repository.UserRepository;
import com.wsiz.ollamawrapper.security.JwtService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

	@Autowired
	private final AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void login(AuthServiceOuterClass.LoginRequest request, StreamObserver<AuthServiceOuterClass.AuthResponse> responseObserver) {
		// Check user credentials
		var user = userRepository.findByLogin(request.getUsername())
				.orElse(null);

		if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			responseObserver.onNext(AuthServiceOuterClass.AuthResponse.newBuilder()
					.setAccessToken(null)
					.setRefreshToken(null)
					.setMessage("Invalid username or password.")
					.setSuccess(false)
					.build());
			responseObserver.onCompleted();
			return;
		}

		// Generate tokens
		var accessToken = jwtService.generateToken(user.getLogin());
		var refreshToken = jwtService.generateRefreshToken(user.getLogin());

		// Send response
		responseObserver.onNext(AuthServiceOuterClass.AuthResponse.newBuilder()
				.setAccessToken(accessToken)
				.setRefreshToken(refreshToken)
				.setMessage("Login successful.")
				.setSuccess(true)
				.build());
		responseObserver.onCompleted();
	}

	@Override
	public void refreshToken(AuthServiceOuterClass.RefreshRequest request, StreamObserver<AuthServiceOuterClass.AuthResponse> responseObserver) {
		if (!jwtService.validateToken(request.getRefreshToken())) {
			responseObserver.onError(new RuntimeException("Invalid refresh token"));
			return;
		}

		var username = jwtService.extractUsername(request.getRefreshToken());
		var newAccessToken = jwtService.generateToken(username);

		var response = AuthServiceOuterClass.AuthResponse.newBuilder()
				.setAccessToken(newAccessToken)
				.setRefreshToken(request.getRefreshToken())
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}

