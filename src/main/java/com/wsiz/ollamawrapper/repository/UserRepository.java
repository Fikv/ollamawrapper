package com.wsiz.ollamawrapper.repository;

import com.wsiz.ollamawrapper.database.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByLogin(String username);
}
