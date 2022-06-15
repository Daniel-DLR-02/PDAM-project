package com.pdam.tcl.repository;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.user.GetUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByNickname(String nickname);

    boolean existsByNickname(String nick);

    boolean existsByEmail(String email);

    @Query(value = """ 
                        SELECT u
                        FROM User u
                        WHERE role = 'ADMIN'
                    """)
    Page<User> getAllAdmins(Pageable pageable);

    @Query(value = """ 
                        SELECT u
                        FROM User u
                    """)
    Page<User> getAllUsers(Pageable pageable);


}
