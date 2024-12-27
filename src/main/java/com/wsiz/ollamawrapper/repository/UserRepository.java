package com.wsiz.ollamawrapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wsiz.ollamawrapper.database.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByLogin(String username);
}
