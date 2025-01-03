package com.wsiz.ollamawrapper.services;

import com.wsiz.ollamawrapper.database.Prompt;
import com.wsiz.ollamawrapper.repository.PromptRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptService {
    private final PromptRepository promptRepository;

    PromptService(PromptRepository promptRepository){
        this.promptRepository = promptRepository;
    }

    public List<Prompt> getAllPrompts() {
        return promptRepository.findAll();
    }

    public Prompt getPromptById(Long id) {
        return promptRepository.findById(id).orElseThrow(() -> new RuntimeException("Prompt not found"));
    }

    @Transactional
    public Prompt createPrompt(Prompt prompt) {
        return promptRepository.save(prompt);
    }

    @Transactional
    public void deletePrompt(Long id) {
        if (!promptRepository.existsById(id)) {
            throw new RuntimeException("Prompt not found with id: " + id);
        }
        promptRepository.deleteById(id);
    }
}
