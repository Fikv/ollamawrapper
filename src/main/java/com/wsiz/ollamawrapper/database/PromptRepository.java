package com.wsiz.ollamawrapper.database;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
}