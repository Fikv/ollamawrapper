package com.wsiz.ollamawrapper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wsiz.ollamawrapper.services.ChatService;

import io.grpc.Server;
import io.grpc.ServerBuilder;

@Configuration
public class GrpcServerConfig {

	@Bean
	public Server grpcServer(ChatService chatService) {
		return ServerBuilder.forPort(9090)
							.addService(chatService)
							.build();
	}
}