package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.UserRole;
import com.pdam.tcl.model.dto.film.GetFilmDto;
import com.pdam.tcl.model.dto.user.CreateUserDto;
import com.pdam.tcl.model.dto.user.EditUserDto;
import com.pdam.tcl.model.dto.user.GetUserDto;
import com.pdam.tcl.model.img.ImgResponse;
import com.pdam.tcl.model.img.ImgurImg;
import com.pdam.tcl.repository.UserRepository;
import com.pdam.tcl.service.ImgServiceStorage;
import com.pdam.tcl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public User saveNoAvatar(CreateUserDto createUsuarioDto) {


        return userRepository.save(User.builder()
                .nombre(createUsuarioDto.getNombre())
                .nickname(createUsuarioDto.getNickName())
                .email(createUsuarioDto.getEmail())
                .fechaNacimiento(createUsuarioDto.getFechaNacimiento())
                .password(passwordEncoder.encode(createUsuarioDto.getPassword()))
                .role(UserRole.USER)
                .build());


    }

    @Override
    public Optional<User> findUserByUuid(UUID uuid){
        return userRepository.findById(uuid);
    }


    @Override
    public User saveAdminNoAvatar(CreateUserDto newUsuario) throws Exception {

        return userRepository.save(User.builder()
                .nombre(newUsuario.getNombre())
                .nickname(newUsuario.getNickName())
                .email(newUsuario.getEmail())
                .fechaNacimiento(newUsuario.getFechaNacimiento())
                .password(passwordEncoder.encode(newUsuario.getPassword()))
                .role(UserRole.ADMIN)
                .build());
    }

    @Override
    public boolean existsById(UUID id) {
        return  userRepository.existsById(id);
    }

    @Override
    public User editUser(UUID id, EditUserDto userDto, MultipartFile file) throws Exception {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        if (user.getAvatar() != null)
            imgServiceStorage.delete(user.getAvatar().getDeletehash());

        ImgResponse img = imgServiceStorage.store(new ImgurImg(Base64.encodeBase64String(file.getBytes()),file.getOriginalFilename()));

        user.setNombre(userDto.getNombre());
        user.setNickname(userDto.getNickName());
        user.setEmail(userDto.getEmail());
        user.setFechaNacimiento(userDto.getFechaNacimiento());
        user.setRole(userDto.getRole());
        user.setAvatar(img.getData());

        return userRepository.save(user);
    }

    @Override
    public User editUserNoAvatar(UUID id, EditUserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        user.setNombre(userDto.getNombre());
        user.setNickname(userDto.getNickName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setFechaNacimiento(userDto.getFechaNacimiento());

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
    public boolean existsByNickname(String nick) {
        return userRepository.existsByNickname(nick);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<GetUserDto> getAllAdmins(Pageable pageable) {
        Page<GetUserDto> pageDto = userRepository.getAllAdmins(pageable).map((u)-> GetUserDto.builder()
                .uuid(u.getUuid())
                .nick(u.getNickname())
                .nombre(u.getNombre())
                .fechaDeNacimiento(u.getFechaNacimiento())
                .email(u.getEmail())
                .role(u.getRole().name())
                .avatar(u.getAvatar()!=null?u.getAvatar().getLink():null)
                .build());

        return pageDto;
    }

    @Override
    public Page<GetUserDto> getAllUsers(Pageable pageable) {
        Page<GetUserDto> pageDto = userRepository.getAllUsers(pageable).map((u)-> GetUserDto.builder()
                .uuid(u.getUuid())
                .nick(u.getNickname())
                .nombre(u.getNombre())
                .fechaDeNacimiento(u.getFechaNacimiento())
                .email(u.getEmail())
                .role(u.getRole().name())
                .avatar(u.getAvatar()!=null?u.getAvatar().getLink():null)
                .build());

        return pageDto;
    }


    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return this.userRepository.findFirstByNickname(nickname)
                .orElseThrow(()-> new UsernameNotFoundException(nickname+ "no encontrado"));
    }
}
