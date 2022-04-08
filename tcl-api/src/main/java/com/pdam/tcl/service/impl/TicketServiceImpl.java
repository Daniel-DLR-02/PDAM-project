package com.pdam.tcl.service.impl;

import com.pdam.tcl.errors.exception.TicketNotFound;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import com.pdam.tcl.repository.TicketRepository;
import com.pdam.tcl.service.TicketService;
import com.pdam.tcl.utils.converters.TicketDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;
    private TicketDtoConverter ticketDtoConverter;

    @Override
    public List<GetTicketDto> getUserTicket() {
        return null;
    }

    @Override
    public GetTicketDto getTicket(UUID id) {
        GetTicketDto findTicket;
        if(ticketRepository.existsById(id))
            findTicket = ticketDtoConverter.ticketDtoToGetDtoConverter(ticketRepository.findById(id).get());
        else
            throw new TicketNotFound("Ticket not found");

        return findTicket;
    }

    @Override
    public GetTicketDto createTicket(CreateTicketDto ticket) {
        return null;
    }

}
