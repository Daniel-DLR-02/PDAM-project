package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.UserRole;
import com.pdam.tcl.model.dto.user.CreateUserDto;
import com.pdam.tcl.repository.UserRepository;
import com.pdam.tcl.service.StorageService;
import com.pdam.tcl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Override
    public User save(CreateUserDto createUsuarioDto, MultipartFile file) throws Exception {

        String fileName = storageService.store(file);

        String uri = storageService.createUri(fileName);

        return userRepository.save(User.builder()
                .nombre(createUsuarioDto.getNombre())
                .nickname(createUsuarioDto.getNickName())
                .email(createUsuarioDto.getEmail())
                .fechaNacimiento(createUsuarioDto.getFechaNacimiento())
                .password(passwordEncoder.encode(createUsuarioDto.getPassword()))
                .avatar(uri)
                .role(UserRole.USER)
                .build());


    }

    @Override
    public Optional<User> findUserByUuid(UUID uuid){
        return userRepository.findById(uuid);
    }


    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.userRepository.findFirstByNickname(nick)
                .orElseThrow(()-> new UsernameNotFoundException(nick+ "no encontrado"));
    }
}
