package com.wsiz.ollamawrapper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wsiz.ollamawrapper.database.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByLogin(String username);

	Optional<User> findByLogin(String username);
}
