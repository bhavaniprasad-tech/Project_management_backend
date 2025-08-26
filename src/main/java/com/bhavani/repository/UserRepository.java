package com.bhavani.repository;

import com.bhavani.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findAllByEmail(String email);
}
