package com.wsiz.ollamawrapper.services;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wsiz.grpc.UserServiceGrpc;
import com.wsiz.grpc.UserServiceOuterClass;
import com.wsiz.ollamawrapper.database.User;
import com.wsiz.ollamawrapper.repository.UserRepository;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

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
					.setMessage("Username or email already in use.")
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

	@Override
	public void getAllUsers(UserServiceOuterClass.EmptyRequest request, StreamObserver<UserServiceOuterClass.UserListResponse> responseObserver) {
		List<User> users = userRepository.findAll();
		UserServiceOuterClass.UserListResponse.Builder responseBuilder = UserServiceOuterClass.UserListResponse.newBuilder();

		for (User user : users) {
			responseBuilder.addUsers(UserServiceOuterClass.User.newBuilder()
					.setId(user.getId().toString())
					.setUsername(user.getLogin())
					.build());
		}

		responseObserver.onNext(responseBuilder.build());
		responseObserver.onCompleted();
	}

	@Override
	public void getUserById(UserServiceOuterClass.UserIdRequest request, StreamObserver<UserServiceOuterClass.UserResponse> responseObserver) {
		User user = userRepository.findById(Long.valueOf(request.getId()))
				.orElseThrow(() -> new RuntimeException("User not found"));

		UserServiceOuterClass.UserResponse response = UserServiceOuterClass.UserResponse.newBuilder()
				.setId(user.getId().toString())
				.setUsername(user.getLogin())
				.setSuccess(true)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void createUser(UserServiceOuterClass.CreateUserRequest request, StreamObserver<UserServiceOuterClass.UserResponse> responseObserver) {
		User user = new User();
		user.setLogin(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user = userRepository.save(user);

		UserServiceOuterClass.UserResponse response = UserServiceOuterClass.UserResponse.newBuilder()
				.setId(user.getId().toString())
				.setUsername(user.getLogin())
				.setMessage("User created successfully!")
				.setSuccess(true)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void deleteUser(UserServiceOuterClass.UserIdRequest request, StreamObserver<UserServiceOuterClass.DeleteResponse> responseObserver) {
		Long userId = Long.valueOf(request.getId());

		if (!userRepository.existsById(userId)) {
			responseObserver.onNext(UserServiceOuterClass.DeleteResponse.newBuilder()
					.setMessage("User not found")
					.setSuccess(false)
					.build());
			responseObserver.onCompleted();
			return;
		}

		userRepository.deleteById(userId);

		responseObserver.onNext(UserServiceOuterClass.DeleteResponse.newBuilder()
				.setMessage("User deleted successfully")
				.setSuccess(true)
				.build());
		responseObserver.onCompleted();
	}
}

