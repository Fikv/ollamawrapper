package com.wsiz.ollamawrapper.services;

import com.wsiz.grpc.PromptServiceGrpc;
import com.wsiz.grpc.PromptServiceOuterClass;
import com.wsiz.ollamawrapper.database.Conversation;
import com.wsiz.ollamawrapper.database.Prompt;
import com.wsiz.ollamawrapper.repository.PromptRepository;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@GrpcService
public class PromptService extends PromptServiceGrpc.PromptServiceImplBase {

    private final PromptRepository promptRepository;

    public PromptService(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    @Override
    public void getAllPrompts(PromptServiceOuterClass.EmptyRequest request, StreamObserver<PromptServiceOuterClass.PromptListResponse> responseObserver) {
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
        prompt.setConversation(new Conversation(request.getConversationId())); // Zakładamy istnienie Conversation
        prompt.setQuestion(request.getQuestion());
        prompt.setAnswer(request.getAnswer());
        prompt.setCreatedDate(LocalDateTime.now());

        prompt = promptRepository.save(prompt);

        responseObserver.onNext(PromptServiceOuterClass.PromptResponse.newBuilder()
                .setPrompt(toProto(prompt))
                .setMessage("Prompt created successfully!")
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void deletePrompt(PromptServiceOuterClass.PromptIdRequest request, StreamObserver<PromptServiceOuterClass.DeleteResponse> responseObserver) {
        if (!promptRepository.existsById(request.getId())) {
            responseObserver.onNext(PromptServiceOuterClass.DeleteResponse.newBuilder()
                    .setMessage("Prompt not found")
                    .setSuccess(false)
                    .build());
            responseObserver.onCompleted();
            return;
        }

        promptRepository.deleteById(request.getId());

        responseObserver.onNext(PromptServiceOuterClass.DeleteResponse.newBuilder()
                .setMessage("Prompt deleted successfully")
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    private PromptServiceOuterClass.Prompt toProto(Prompt prompt) {
        return PromptServiceOuterClass.Prompt.newBuilder()
                .setId(prompt.getId())
                .setConversationId(prompt.getConversation().getId())
                .setQuestion(prompt.getQuestion())
                .setAnswer(prompt.getAnswer())
                .setCreatedDate(prompt.getCreatedDate().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }
}