package com.pdam.tcl.service.impl;

import com.pdam.tcl.errors.exception.SessionNotFoundException;
import com.pdam.tcl.errors.exception.TicketNotFound;
import com.pdam.tcl.errors.exception.UserNotFoundException;
import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.Ticket;
import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.ticket.CreateTicketDto;
import com.pdam.tcl.model.dto.ticket.GetTicketDto;
import com.pdam.tcl.repository.TicketRepository;
import com.pdam.tcl.repository.UserRepository;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.service.TicketService;
import com.pdam.tcl.service.UserService;
import com.pdam.tcl.utils.converters.TicketDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketDtoConverter ticketDtoConverter;
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final UserService userService;

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
    public GetTicketDto createTicket(CreateTicketDto ticket, UUID userUuid) {
        Optional<Session> session = sessionService.findById(ticket.getSessionUuid());
        Optional<User> user = userService.findUserByUuid(userUuid);

        if(session.isPresent() && user.isPresent()) {

            Ticket newTicket = Ticket.builder()
                    .session(session.get())
                    .hallColumn(ticket.getColumn())
                    .hallRow(ticket.getRow())
                    .build();

            user.get().addTicket(newTicket);
            Ticket savedTicket = ticketRepository.save(newTicket);
            userRepository.save(user.get());

            session.get().getAvailableSeats()[ticket.getRow()][ticket.getColumn()] = "O";
            return ticketDtoConverter.ticketDtoToGetDtoConverter(savedTicket);
        }
        else{
            throw new SessionNotFoundException("Session not found");
        }
    }


    @Override
    public boolean existsById(UUID idTicket) {
        return ticketRepository.existsById(idTicket);
    }

    @Override
    public Page<Ticket> getTicketsByUserId(UUID idUser, Pageable pageable) {
        return ticketRepository.findAllByUserId(idUser, pageable);
    }

    @Override
    public void deleteTicket(UUID idTicket) {
        Optional<Ticket> ticket = ticketRepository.findById(idTicket);

        ticket.ifPresent(ticketRepository::delete);
    }

    @Override
    public GetTicketDto editTicket(UUID id, CreateTicketDto newTicket) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFound("Ticket not found"));
        ticket.setSession(sessionService.findById(newTicket.getSessionUuid()).orElseThrow(() -> new SessionNotFoundException("Session not found")));
        ticket.setHallColumn(newTicket.getColumn());
        ticket.setHallRow(newTicket.getRow());
        return ticketDtoConverter.ticketDtoToGetDtoConverter(ticketRepository.save(ticket));
    }


}
