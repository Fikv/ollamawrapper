package com.wsiz.ollamawrapper.service;

import java.util.List;
import java.util.Optional;

import com.wsiz.grpc.ConversationServiceGrpc;
import com.wsiz.grpc.ConversationServiceOuterClass;
import com.wsiz.ollamawrapper.entity.Conversation;
import com.wsiz.ollamawrapper.exception.ResourceNotFoundException;
import com.wsiz.ollamawrapper.repository.ConversationRepository;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class ConversationService extends ConversationServiceGrpc.ConversationServiceImplBase {

    private final ConversationRepository conversationRepository;
    private final UserServiceImpl userService;

    @Override
    public void getAllConversations(ConversationServiceOuterClass.Empty request, StreamObserver<ConversationServiceOuterClass.ConversationListResponse> responseObserver) {
        List<Conversation> conversations = conversationRepository.findAll();
        ConversationServiceOuterClass.ConversationListResponse.Builder responseBuilder = ConversationServiceOuterClass.ConversationListResponse.newBuilder();

        for (Conversation conversation : conversations) {
            responseBuilder.addConversations(toProto(conversation));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getConversationById(ConversationServiceOuterClass.ConversationIdRequest request, StreamObserver<ConversationServiceOuterClass.ConversationResponse> responseObserver) {
        Optional<Conversation> conversationOptional = conversationRepository.findById(request.getId());

        if (conversationOptional.isEmpty()) {
            responseObserver.onNext(ConversationServiceOuterClass.ConversationResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Conversation not found")
                    .build());
            responseObserver.onCompleted();
            return;
        }

        responseObserver.onNext(ConversationServiceOuterClass.ConversationResponse.newBuilder()
                .setConversation(toProto(conversationOptional.get()))
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void createConversation(ConversationServiceOuterClass.CreateConversationRequest request, StreamObserver<ConversationServiceOuterClass.ConversationResponse> responseObserver) {
        Conversation conversation = new Conversation();
        conversation.setTopic(request.getTopic());
        conversation.setModel(request.getModel());

        conversation.setUser((userService.findById(request.getUserId())));

        conversation = conversationRepository.save(conversation);

        responseObserver.onNext(ConversationServiceOuterClass.ConversationResponse.newBuilder()
                .setConversation(toProto(conversation))
                .setMessage("Conversation created successfully!")
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteConversation(ConversationServiceOuterClass.ConversationIdRequest request, StreamObserver<ConversationServiceOuterClass.ConversationResponse> responseObserver) {
        if (!conversationRepository.existsById(request.getId())) {
            responseObserver.onNext(ConversationServiceOuterClass.ConversationResponse.newBuilder()
                    .setMessage("Conversation not found")
                    .setSuccess(false)
                    .build());
            responseObserver.onCompleted();
            return;
        }

        conversationRepository.deleteById(request.getId());

        responseObserver.onNext(ConversationServiceOuterClass.ConversationResponse.newBuilder()
                .setMessage("Conversation deleted successfully")
                .setSuccess(true)
                .build());
        responseObserver.onCompleted();
    }

    private ConversationServiceOuterClass.Conversation toProto(Conversation conversation) {
        return ConversationServiceOuterClass.Conversation.newBuilder()
                .setId(conversation.getId())
                .setUserId(conversation.getUser().getId())
                .setTopic(conversation.getTopic())
                .setModel(conversation.getModel())
                .build();
    }

    public Conversation findById(long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found."));
    }
}
