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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public User saveAdmin(CreateUserDto newUsuario, MultipartFile file) throws Exception {
        String fileName = storageService.store(file);

        String uri = storageService.createUri(fileName);

        return userRepository.save(User.builder()
                .nombre(newUsuario.getNombre())
                .nickname(newUsuario.getNickName())
                .email(newUsuario.getEmail())
                .fechaNacimiento(newUsuario.getFechaNacimiento())
                .password(passwordEncoder.encode(newUsuario.getPassword()))
                .avatar(uri)
                .role(UserRole.ADMIN)
                .build());
    }

    @Override
    public boolean existsById(UUID id) {
        return  userRepository.existsById(id);
    }

    @Override
    public User editUser(UUID id, CreateUserDto userDto, MultipartFile file) throws Exception {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        String fileName = storageService.store(file);

        String uri = storageService.createUri(fileName);

        user.setNombre(userDto.getNombre());
        user.setNickname(userDto.getNickName());
        user.setEmail(userDto.getEmail());
        user.setFechaNacimiento(userDto.getFechaNacimiento());
        user.setPassword(userDto.getPassword());
        user.setAvatar(uri);
        user.setRole(UserRole.valueOf(userDto.getRole()));

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) throws IOException {

        Optional<User> usuarioABorrar = userRepository.findById(id);

        if((usuarioABorrar.isPresent()) &&
        (usuarioABorrar.get().getAvatar() != null)){

            String filePathString = "./uploads/"+usuarioABorrar.get().getAvatar().replace("http://localhost:8080/download/","");
            Path path = Paths.get(filePathString);

            storageService.deleteFile(path);
        }

        userRepository.deleteById(id);

    }


    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
        return this.userRepository.findFirstByNickname(nick)
                .orElseThrow(()-> new UsernameNotFoundException(nick+ "no encontrado"));
    }
}
