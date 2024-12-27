package com.wsiz.ollamawrapper.services;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.wsiz.grpc.AuthResponse;
import com.wsiz.grpc.AuthServiceGrpc;
import com.wsiz.grpc.LoginRequest;
import com.wsiz.grpc.RefreshRequest;
import com.wsiz.ollamawrapper.security.JwtService;

@GrpcService
@RequiredArgsConstructor
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

	private AuthenticationManager authenticationManager;

	private JwtService jwtService;

	@Override
	public void login(LoginRequest request, StreamObserver<AuthResponse> responseObserver) {
		var authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);

		var accessToken = jwtService.generateToken(authentication.getName());
		var refreshToken = jwtService.generateRefreshToken(authentication.getName());

		var response = AuthResponse.newBuilder()
				.setAccessToken(accessToken)
				.setRefreshToken(refreshToken)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void refreshToken(RefreshRequest request, StreamObserver<AuthResponse> responseObserver) {
		if (!jwtService.validateToken(request.getRefreshToken())) {
			responseObserver.onError(new RuntimeException("Invalid refresh token"));
			return;
		}

		var username = jwtService.extractUsername(request.getRefreshToken());
		var newAccessToken = jwtService.generateToken(username);

		var response = AuthResponse.newBuilder()
				.setAccessToken(newAccessToken)
				.setRefreshToken(request.getRefreshToken())
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
