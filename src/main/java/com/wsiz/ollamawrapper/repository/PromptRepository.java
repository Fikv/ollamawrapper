package com.wsiz.ollamawrapper.repository;

import com.wsiz.ollamawrapper.database.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
}

