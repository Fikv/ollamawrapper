package com.wsiz.ollamawrapper.services;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.wsiz.grpc.UserServiceGrpc;
import com.wsiz.grpc.UserServiceOuterClass;
import com.wsiz.ollamawrapper.database.User;
import com.wsiz.ollamawrapper.repository.UserRepository;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

	private final UserRepository userRepository; // Assume this is a JPA repository for saving users
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void register(UserServiceOuterClass.RegisterRequest request, StreamObserver<UserServiceOuterClass.RegisterResponse> responseObserver) {
		// Check if the username is already taken
		if (userRepository.existsByLogin(request.getUsername())) {
			responseObserver.onNext(UserServiceOuterClass.RegisterResponse.newBuilder()
					.setMessage("Username already in use.")
					.setSuccess(false)
					.build());
			responseObserver.onCompleted();
			return;
		}

		// Save new user
		User user = new User();
		user.setLogin(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password

		userRepository.save(user);

		// Respond to the client
		responseObserver.onNext(UserServiceOuterClass.RegisterResponse.newBuilder()
				.setMessage("User registered successfully!")
				.setSuccess(true)
				.build());
		responseObserver.onCompleted();
	}
}

