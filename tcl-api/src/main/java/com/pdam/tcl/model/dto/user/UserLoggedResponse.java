package com.pdam.tcl.model.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoggedResponse {

    private String nickName;
    private String avatar;
    private String token;
    private String email;
}
