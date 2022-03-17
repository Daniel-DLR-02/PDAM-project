package com.pdam.tcl.repository;

import com.pdam.tcl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByNickname(String nickname);
}
