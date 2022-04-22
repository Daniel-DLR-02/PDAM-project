package com.pdam.tcl.utils.converters;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.user.GetUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    public GetUserDto userToGetUserDto(User user){
        return GetUserDto.builder()
                .nick(user.getNickname())
                .nombre(user.getNombre())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .fechaDeNacimiento(user.getFechaNacimiento())
                .build();

    }
}
