package com.wsiz.ollamawrapper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.wsiz.grpc.AuthServiceGrpc;
import com.wsiz.grpc.AuthServiceOuterClass;
import com.wsiz.ollamawrapper.security.JwtService;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
@RequiredArgsConstructor
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	@Override
	public void login(AuthServiceOuterClass.LoginRequest request, StreamObserver<AuthServiceOuterClass.AuthResponse> responseObserver) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);

		var accessToken = jwtService.generateToken(authentication.getName());
		var refreshToken = jwtService.generateRefreshToken(authentication.getName());

		var response = AuthServiceOuterClass.AuthResponse.newBuilder()
				.setAccessToken(accessToken)
				.setRefreshToken(refreshToken)
				.build();

		responseObserver.onNext(response);
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

