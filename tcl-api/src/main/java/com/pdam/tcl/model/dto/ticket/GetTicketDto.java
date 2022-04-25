package com.pdam.tcl.model.dto.ticket;

import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.user.GetUserDto;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetTicketDto {
    private UUID uuid;
    private GetUserDto user;
    private Session session;
    private int row;
    private int column;
}
