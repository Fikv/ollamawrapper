package com.wsiz.ollamawrapper.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.wsiz.grpc.PromptServiceGrpc;
import com.wsiz.grpc.PromptServiceOuterClass;
import com.wsiz.ollamawrapper.entity.Prompt;
import com.wsiz.ollamawrapper.repository.PromptRepository;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class PromptService extends PromptServiceGrpc.PromptServiceImplBase {

    private final PromptRepository promptRepository;
    private final ConversationService conversationService;

    @Override
    public void getAllPrompts(PromptServiceOuterClass.EmptyRequestPrompt request, StreamObserver<PromptServiceOuterClass.PromptListResponse> responseObserver) {
        List<Prompt> prompts = promptRepository.findAll();
        PromptServiceOuterClass.PromptListResponse.Builder responseBuilder = PromptServiceOuterClass.PromptListResponse.newBuilder();

        for (Prompt prompt : prompts) {
            responseBuilder.addPrompts(toProto(prompt));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPromptById(PromptServiceOuterClass.PromptIdRequest request, StreamObserver<PromptServiceOuterClass.PromptResponse> responseObserver) {
        Optional<Prompt> promptOptional = promptRepository.findById(request.getId());

        if (promptOptional.isEmpty()) {
            responseObserver.onNext(PromptServiceOuterClass.PromptResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Prompt not found")
                    .build());
            responseObserver.onCompleted();
            return;
        }

        responseObserver.onNext(PromptServiceOuterClass.PromptResponse.newBuilder()
                .setPrompt(toProto(promptOptional.get()))
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void createPrompt(PromptServiceOuterClass.CreatePromptRequest request, StreamObserver<PromptServiceOuterClass.PromptResponse> responseObserver) {
        Prompt prompt = new Prompt();
        prompt.setConversation(conversationService.findById(request.getConversationId()));
        prompt.setQuestion(request.getQuestion());
        prompt.setAnswer(request.getAnswer());
        prompt.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()).getTime());

        promptRepository.save(prompt);

        responseObserver.onNext(PromptServiceOuterClass.PromptResponse.newBuilder()
                .setPrompt(toProto(prompt))
                .setMessage("Prompt created successfully!")
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void deletePrompt(PromptServiceOuterClass.PromptIdRequest request, StreamObserver<PromptServiceOuterClass.DeleteResponsePrompt> responseObserver) {
        if (!promptRepository.existsById(request.getId())) {
            responseObserver.onNext(PromptServiceOuterClass.DeleteResponsePrompt.newBuilder()
                    .setMessage("Prompt not found")
                    .setSuccess(false)
                    .build());
            responseObserver.onCompleted();
            return;
        }

        promptRepository.deleteById(request.getId());

        responseObserver.onNext(PromptServiceOuterClass.DeleteResponsePrompt.newBuilder()
                .setMessage("Prompt deleted successfully")
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    private PromptServiceOuterClass.Prompt toProto(Prompt prompt) {
        var instant = Instant.ofEpochMilli(prompt.getCreatedDate());
        var dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return PromptServiceOuterClass.Prompt.newBuilder()
                .setId(prompt.getId())
                .setConversationId(prompt.getConversation().getId())
                .setQuestion(prompt.getQuestion())
                .setAnswer(prompt.getAnswer())
                .setCreatedDate(String.valueOf(dateTime))
                .build();
    }
}