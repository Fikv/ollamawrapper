package com.wsiz.ollamawrapper.services;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Map;

import org.springframework.ai.ollama.OllamaChatModel;

import com.wsiz.grpc.ChatServiceOuterClass;
import com.wsiz.grpc.ChatServiceGrpc;

@GrpcService
@RequiredArgsConstructor
public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {

	private final OllamaChatModel ollamaChatModel;

	@Override
	public void generate(ChatServiceOuterClass.ChatRequest request, StreamObserver<ChatServiceOuterClass.ChatResponse> responseObserver) {
		try {
			var inputMessage = request.getMessage();
			if (inputMessage == null || inputMessage.trim().isEmpty()) {
				responseObserver.onError(new IllegalArgumentException("Message cannot be null or empty"));
				return;
			}

			var respMap = Map.of("generation", this.ollamaChatModel.call(inputMessage));

			var response = ChatServiceOuterClass.ChatResponse.newBuilder()
					.setGeneration(respMap.get("generation"))
					.build();

			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	@Override
	public void generateStream(ChatServiceOuterClass.ChatRequest request,
							   StreamObserver<ChatServiceOuterClass.ChatResponse> responseObserver) {
		try {
			var inputMessage = request.getMessage();
			if (inputMessage == null || inputMessage.trim().isEmpty()) {
				responseObserver.onError(new IllegalArgumentException("Message cannot be null or empty"));
				return;
			}

			ollamaChatModel.stream(inputMessage).doOnTerminate(() -> responseObserver.onCompleted()).subscribe(partialResponse -> {
				var response = ChatServiceOuterClass.ChatResponse.newBuilder()
						.setGeneration(partialResponse)
						.build();
				responseObserver.onNext(response);
			});

		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}
}