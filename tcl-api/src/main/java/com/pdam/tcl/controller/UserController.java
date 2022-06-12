package com.pdam.tcl.controller;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import com.pdam.tcl.model.dto.user.*;
import com.pdam.tcl.security.jwt.JwtProvider;
import com.pdam.tcl.service.TicketService;
import com.pdam.tcl.service.UserService;
import com.pdam.tcl.utils.converters.UserDtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Esta clase implementa los endpoints para User")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    @Operation(summary = "Registra un usuario en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente el usuario",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetUserDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado al crear el usuario.",
                    content = @Content),
    })
    @PostMapping("/auth/register")
    public ResponseEntity<GetUserDto> doRegister(@Nullable @RequestPart("file") MultipartFile file,
                                                 @RequestPart("user") @Valid CreateUserDto newUsuario) throws Exception{
        User saved = (file==null || file.isEmpty())?userService.saveNoAvatar(newUsuario):userService.save(newUsuario,file);

        if(saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.userToGetUserDto(saved));
    }

    @Operation(summary = "Registra un usuario administrador en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente el usuario administrador",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetUserDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado al crear el usuario.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),
    })
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

    @Operation(summary = "Loguea a un usuario registrado en la base de datos proporcionando un token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha iniciado sesión correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserLoggedResponse.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Las credenciales no son correctas.",
                    content = @Content),
    })
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

    @Operation(summary = "Edita un usuario en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado correctamente el usuario buscado.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTicketDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario cuyo UUID coincida con el especificado en la url.",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado a la hora de editar el usuario registrado.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),
    })
    @PutMapping("/user/{id}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable("id") UUID id,@Valid @RequestPart("user") EditUserDto userDto,@Nullable @RequestPart("file") MultipartFile file) throws Exception{
        if(file==null || file.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.userToGetUserDto(userService.editUserNoAvatar(id, userDto)));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.userToGetUserDto(userService.editUser(id, userDto, file)));
        }
    }

    @Operation(summary = "Edita el usuario logueado actualmente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado correctamente el usuario.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTicketDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Algo ha fallado a la hora de editar el usuario registrado.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),

    })
    @PutMapping("/user")
    public ResponseEntity<GetUserDto> editCurrentUser(@AuthenticationPrincipal User currentUser,@Valid @RequestPart("user") EditUserDto userDto, @Nullable @RequestPart("file") MultipartFile file) throws Exception{
        if(file==null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.userToGetUserDto(userService.editUserNoAvatar(currentUser.getUuid(), userDto)));
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(userDtoConverter.userToGetUserDto(userService.editUser(currentUser.getUuid(), userDto, file)));
        }
    }

    @Operation(summary = "Obtiene los datos del usuario logueado actualmente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestran los datos del usuario logueado.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetUserDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/me")
    public ResponseEntity<GetUserDto> me(@AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(userDtoConverter.userToGetUserDto(currentUser));
    }

    @Operation(summary = "Obtiene los datos de un usuario en base a su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado los datos del usuario correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetUserDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "El usuario buscado no se encuentra registrado en la base de datos.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<GetUserDto> findUser(@PathVariable("id") UUID id){
        if(userService.existsById(id))
            return ResponseEntity.ok(userDtoConverter.userToGetUserDto(userService.findUserByUuid(id).get()));
        else
            throw new UsernameNotFoundException("Resquested user not found");
    }

    @Operation(summary = "Elimina un usuario especificada por url.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado el usuario especificado de manera idempotente.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),
    })
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID id) throws IOException {
        if(userService.existsById(id)){
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else
            throw new UsernameNotFoundException("Resquested user not found");
    }

    @Operation(summary = "Obtiene los datos de todos los usuario administradores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se muestran los datos de los usuario administradores correctamente.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetUserDto.class))}),
            @ApiResponse(responseCode = "401",
                    description = "Falta autorización.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Faltan permisos necesarios.",
                    content = @Content),
    })
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
