package com.wsiz.ollamawrapper.repository;

import com.wsiz.ollamawrapper.database.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}

