package com.pdam.tcl.controller;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.user.CreateUserDto;
import com.pdam.tcl.model.dto.user.GetUserDto;
import com.pdam.tcl.model.dto.user.LoginDto;
import com.pdam.tcl.model.dto.user.UserLoggedResponse;
import com.pdam.tcl.security.jwt.JwtProvider;
import com.pdam.tcl.service.TicketService;
import com.pdam.tcl.service.UserService;
import com.pdam.tcl.utils.converters.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    private final TicketService ticketService;

    @PostMapping("/auth/register")
    public ResponseEntity<GetUserDto> doRegister(@RequestPart("file") MultipartFile file,
                                                 @RequestPart("user") @Valid CreateUserDto newUsuario) throws Exception{
        User saved = userService.save(newUsuario,file);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.userToGetUserDto(saved));
    }

    @PostMapping("/auth/login")
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

    @PostMapping("/user/new-admin")
    public ResponseEntity<GetUserDto> registerAdmin(@RequestPart("file") MultipartFile file,
                                                 @RequestPart("user") @Valid CreateUserDto newUsuario) throws Exception{

        User saved = userService.saveAdmin(newUsuario,file);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.userToGetUserDto(saved));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable("id") UUID id,@RequestBody CreateUserDto userDto,MultipartFile file) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.userToGetUserDto(userService.editUser(id,userDto,file)));
    }

    @PutMapping("/user")
    public ResponseEntity<GetUserDto> editCurrentUser(@AuthenticationPrincipal User currentUser,@RequestBody CreateUserDto userDto,MultipartFile file) throws Exception{
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.userToGetUserDto(userService.editUser(currentUser.getUuid(),userDto,file)));
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserDto> me(@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(userDtoConverter.userToGetUserDto(currentUser));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GetUserDto> findUser(@PathVariable("id") UUID id){
        if(userService.existsById(id))
            return ResponseEntity.ok(userDtoConverter.userToGetUserDto(userService.findUserByUuid(id).get()));
        else
            throw new UsernameNotFoundException("Resquested user not found");
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID id) throws IOException {
        if(userService.existsById(id)){
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else
            throw new UsernameNotFoundException("Resquested user not found");
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
