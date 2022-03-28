package com.pdam.tcl.service;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.user.CreateUserDto;
import com.pdam.tcl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;


public interface UserService {

    User save(CreateUserDto createUserDto, MultipartFile file) throws Exception;

    Optional<User> findUserByUuid(UUID uuid);

}