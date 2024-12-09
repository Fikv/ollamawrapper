package com.wsiz.ollamawrapper.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/prompts")
public class PromptController {
    @Autowired
    private PromptRepository promptRepository;

    @GetMapping
    public List<Prompt> getAllPrompts() {
        return promptRepository.findAll();
    }

    @PostMapping
    public Prompt createPrompt(@RequestBody Prompt prompt) {
        return promptRepository.save(prompt);
    }

    @GetMapping("/{id}")
    public Prompt getPromptsById(@PathVariable Long id) {
        return promptRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Prompt updatePrompt(@PathVariable Long id, @RequestBody Prompt userDetails) {
        Prompt prompt = promptRepository.findById(id).orElse(null);
        if (prompt != null) {
            prompt.setQuestion(userDetails.getQuestion());
            prompt.setAnswer(userDetails.getAnswer());
            return promptRepository.save(prompt);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePrompt(@PathVariable Long id) {
        promptRepository.deleteById(id);
    }
}