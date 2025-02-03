package com.wsiz.ollamawrapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wsiz.ollamawrapper.entity.Prompt;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
}

