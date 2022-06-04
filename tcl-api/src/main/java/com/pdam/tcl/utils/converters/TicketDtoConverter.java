package com.pdam.tcl.utils.converters;

import com.pdam.tcl.model.Ticket;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketDtoConverter {

    private final UserDtoConverter userDtoConverter;
    private final SessionDtoConverter sessionDtoConverter;

    public GetTicketDto ticketDtoToGetDtoConverter(Ticket ticket){
        return GetTicketDto.builder()
                .uuid(ticket.getUuid())
                .user(userDtoConverter.userToGetUserDto(ticket.getUser()))
                .session(sessionDtoConverter.sessionToGetSessionDataDto(ticket.getSession()))
                .row(ticket.getHallRow())
                .column(ticket.getHallColumn())
                .build();
    }
}
