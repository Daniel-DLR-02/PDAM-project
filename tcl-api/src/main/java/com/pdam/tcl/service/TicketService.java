package com.pdam.tcl.service;

import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    public List<GetTicketDto> getUserTicket();

    public GetTicketDto getTicket(UUID id);

    public GetTicketDto createTicket(CreateTicketDto ticket, User user);

    boolean existsById(UUID idTicket);
}
