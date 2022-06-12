package com.pdam.tcl.controller;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.user.*;
import com.pdam.tcl.security.jwt.JwtProvider;
import com.pdam.tcl.service.TicketService;
import com.pdam.tcl.service.UserService;
import com.pdam.tcl.utils.converters.UserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    public ResponseEntity<GetUserDto> doRegister(@Nullable @RequestPart("file") MultipartFile file,
                                                 @RequestPart("user") @Valid CreateUserDto newUsuario) throws Exception{
        User saved = (file==null || file.isEmpty())?userService.saveNoAvatar(newUsuario):userService.save(newUsuario,file);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.userToGetUserDto(saved));
    }

    @PostMapping("/user/new-admin")
    public ResponseEntity<GetUserDto> registerAdmin(@Nullable @RequestPart("file") MultipartFile file,
                                                    @RequestPart("user") @Valid CreateUserDto newUsuario) throws Exception {

        System.out.println(file==null || file.isEmpty());

        User saved = (file==null || file.isEmpty())?userService.saveAdminNoAvatar(newUsuario):userService.saveAdmin(newUsuario,file);

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
                                loginDto.getNickname(),
                                loginDto.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        String jwt = jwtProvider.generateToken(authentication);


        User user = (User) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.OK)
                .body(convertUserUserLoggedResponse(user, jwt));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable("id") UUID id,@Valid @RequestPart("user") EditUserDto userDto,@Nullable @RequestPart("file") MultipartFile file) throws Exception{
        if(file==null || file.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.userToGetUserDto(userService.editUserNoAvatar(id, userDto)));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.userToGetUserDto(userService.editUser(id, userDto, file)));
        }
    }

    @PutMapping("/user")
    public ResponseEntity<GetUserDto> editCurrentUser(@AuthenticationPrincipal User currentUser,@Valid @RequestPart("user") EditUserDto userDto, @Nullable @RequestPart("file") MultipartFile file) throws Exception{
        if(file==null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.userToGetUserDto(userService.editUserNoAvatar(currentUser.getUuid(), userDto)));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.userToGetUserDto(userService.editUser(currentUser.getUuid(), userDto, file)));
        }
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

    @GetMapping("/user/admin")
    public ResponseEntity<Page<GetUserDto>> getUserAdmin(@PageableDefault(size = 30) Pageable pageable){
        return ResponseEntity.ok(userService.getAllAdmins(pageable));
    }


    private UserLoggedResponse convertUserUserLoggedResponse(User user, String jwt) {
        return UserLoggedResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .avatar(user.getAvatar()!=null?user.getAvatar().getLink():"")
                .role(user.getRole().name())
                .token(jwt)
                .build();
    }
}
