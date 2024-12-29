package com.wsiz.ollamawrapper.services;

import com.wsiz.ollamawrapper.database.Conversation;
import com.wsiz.ollamawrapper.repository.ConversationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;

    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id).orElseThrow(() -> new RuntimeException("Conversation not found"));
    }

    @Transactional
    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }

    @Transactional
    public void deletePrompt(Long id) {
        if (!conversationRepository.existsById(id)) {
            throw new RuntimeException("Conversation not found with id: " + id);
        }
        conversationRepository.deleteById(id);
    }
}

