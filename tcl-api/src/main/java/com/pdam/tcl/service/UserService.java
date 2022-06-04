package com.pdam.tcl.service;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.user.CreateUserDto;
import com.pdam.tcl.model.dto.user.EditUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


public interface UserService {

    User save(CreateUserDto createUserDto, MultipartFile file) throws Exception;

    User saveNoAvatar(CreateUserDto createUsuarioDto);

    Optional<User> findUserByUuid(UUID uuid);

    User saveAdmin(CreateUserDto newUsuario, MultipartFile file) throws Exception;

    User saveAdminNoAvatar(CreateUserDto newUsuario) throws Exception;

    boolean existsById(UUID id);

    User editUser(UUID id, EditUserDto userDto, MultipartFile file) throws Exception;

    User editUserNoAvatar(UUID id, EditUserDto userDto);

    void deleteUser(UUID id) throws IOException;
}
