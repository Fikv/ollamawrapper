package com.wsiz.ollamawrapper.repository;

import com.wsiz.ollamawrapper.database.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}

