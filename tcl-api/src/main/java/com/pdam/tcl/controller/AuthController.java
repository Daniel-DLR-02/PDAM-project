package com.pdam.tcl.controller;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.user.CreateUserDto;
import com.pdam.tcl.model.dto.user.GetUserDto;
import com.pdam.tcl.model.dto.user.LoginDto;
import com.pdam.tcl.model.dto.user.UserLoggedResponse;
import com.pdam.tcl.security.jwt.JwtProvider;
import com.pdam.tcl.service.UserService;
import com.pdam.tcl.utils.converters.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    @PostMapping("/register")
    public ResponseEntity<GetUserDto> doRegister(@RequestPart("file") MultipartFile file,
                                                 @RequestPart("user") @Valid CreateUserDto newUsuario) throws Exception{
        User saved = userService.save(newUsuario,file);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.userToGetUserDto(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoggedResponse> doLogin(@RequestBody LoginDto loginDto){
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getNickName(),
                                loginDto.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        String jwt = jwtProvider.generateToken(authentication);


        User user = (User) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.OK)
                .body(convertUserUserLoggedResponse(user, jwt));
    }

    private UserLoggedResponse convertUserUserLoggedResponse(User user, String jwt) {
        return UserLoggedResponse.builder()
                .nickName(user.getNickname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .token(jwt)
                .build();
    }
}
