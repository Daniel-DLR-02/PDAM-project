package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.UserRole;
import com.pdam.tcl.model.dto.user.CreateUserDto;
import com.pdam.tcl.model.img.ImgResponse;
import com.pdam.tcl.model.img.ImgurImg;
import com.pdam.tcl.repository.UserRepository;
import com.pdam.tcl.service.ImgServiceStorage;
import com.pdam.tcl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImgServiceStorage imgServiceStorage;


    @Override
    public User save(CreateUserDto createUsuarioDto, MultipartFile file) throws Exception {

        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        return userRepository.save(User.builder()
                .nombre(createUsuarioDto.getNombre())
                .nickname(createUsuarioDto.getNickName())
                .email(createUsuarioDto.getEmail())
                .fechaNacimiento(createUsuarioDto.getFechaNacimiento())
                .password(passwordEncoder.encode(createUsuarioDto.getPassword()))
                .avatar(img.getData())
                .role(UserRole.USER)
                .build());


    }

    @Override
    public Optional<User> findUserByUuid(UUID uuid){
        return userRepository.findById(uuid);
    }

    @Override
    public User saveAdmin(CreateUserDto newUsuario, MultipartFile file) throws Exception {
        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        return userRepository.save(User.builder()
                .nombre(newUsuario.getNombre())
                .nickname(newUsuario.getNickName())
                .email(newUsuario.getEmail())
                .fechaNacimiento(newUsuario.getFechaNacimiento())
                .password(passwordEncoder.encode(newUsuario.getPassword()))
                .avatar(img.getData())
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

        imgServiceStorage.delete(user.getAvatar().getDeletehash());

        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        user.setNombre(userDto.getNombre());
        user.setNickname(userDto.getNickName());
        user.setEmail(userDto.getEmail());
        user.setFechaNacimiento(userDto.getFechaNacimiento());
        user.setPassword(userDto.getPassword());
        user.setAvatar(img.getData());
        user.setRole(UserRole.valueOf(userDto.getRole()));

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) throws IOException {

        Optional<User> usuarioABorrar = userRepository.findById(id);

        if((usuarioABorrar.isPresent()) &&
        (usuarioABorrar.get().getAvatar() != null)){

            imgServiceStorage.delete(usuarioABorrar.get().getAvatar().getDeletehash());

        }

        userRepository.deleteById(id);

    }


    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return this.userRepository.findFirstByNickname(nickname)
                .orElseThrow(()-> new UsernameNotFoundException(nickname+ "no encontrado"));
    }
}
