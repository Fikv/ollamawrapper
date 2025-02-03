package com.wsiz.ollamawrapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wsiz.ollamawrapper.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}

