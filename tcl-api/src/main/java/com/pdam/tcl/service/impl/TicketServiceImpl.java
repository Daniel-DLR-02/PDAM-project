package com.pdam.tcl.service.impl;

import com.pdam.tcl.errors.exception.SessionNotFoundException;
import com.pdam.tcl.errors.exception.TicketNotFound;
import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.Ticket;
import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import com.pdam.tcl.repository.TicketRepository;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.service.TicketService;
import com.pdam.tcl.utils.converters.TicketDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;
    private TicketDtoConverter ticketDtoConverter;
    private SessionService sessionService;

    @Override
    public List<GetTicketDto> getUserTicket() {
        return null;
    }

    @Override
    public GetTicketDto getTicket(UUID id) {
        GetTicketDto findTicket;

        Optional<Ticket> ticket = ticketRepository.findById(id);

        if(ticket.isPresent())
            findTicket = ticketDtoConverter.ticketDtoToGetDtoConverter(ticket.get());
        else
            throw new TicketNotFound("Ticket not found");

        return findTicket;
    }

    @Override
    public GetTicketDto createTicket(CreateTicketDto ticket, User user) {
        Optional<Session> session = sessionService.findById(ticket.getSessionUuid());

        if(session.isPresent()) {

            Ticket newTicket = Ticket.builder()
                    .session(session.get())
                    .user(user)
                    .hallColumn(ticket.getColumn())
                    .hallRow(ticket.getRow())
                    .build();

            ticketRepository.save(newTicket);
            session.get().getAvailableSeats()[ticket.getRow()][ticket.getColumn()] = true;

            return ticketDtoConverter.ticketDtoToGetDtoConverter(newTicket);
        }
        else{
            throw new SessionNotFoundException("Session not found");
        }
    }


    @Override
    public boolean existsById(UUID idTicket) {
        return ticketRepository.existsById(idTicket);
    }

}
