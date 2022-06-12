package com.pdam.tcl.service.impl;

import com.pdam.tcl.errors.exception.FilmNotFoundException;
import com.pdam.tcl.errors.exception.HallNotFoundException;
import com.pdam.tcl.errors.exception.SessionNotFoundException;
import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.Hall;
import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.User;
import com.pdam.tcl.model.dto.session.CreateSessionDto;
import com.pdam.tcl.model.dto.session.GetSessionDto;
import com.pdam.tcl.repository.FilmRepository;
import com.pdam.tcl.repository.HallRepository;
import com.pdam.tcl.repository.SessionRepository;
import com.pdam.tcl.repository.TicketRepository;
import com.pdam.tcl.service.*;
import com.pdam.tcl.utils.converters.SessionDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SessionDtoConverter sessionDtoConverter;
    private final TicketRepository ticketRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    @Override
    public boolean existsById(UUID id) {
        return sessionRepository.existsById(id);
    }

    @Override
    public Optional<Session> findById(UUID sessionUuid) {
        return sessionRepository.findById(sessionUuid);
    }

    @Override
    public void deleteById(UUID sessionUuid) {

        ticketRepository.getAllTicketsFromASession(sessionUuid).forEach(ticket -> ticketRepository.delete(ticket));

        sessionRepository.deleteById(sessionUuid);
    }

    @Override
    public Session save(CreateSessionDto createSessionDto) {
        Optional<Film> film = filmRepository.findById(createSessionDto.getFilmUuid());
        Optional<Hall> hall = hallRepository.findById(createSessionDto.getHallUuid());
        if(film.isPresent()) {
            if(hall.isPresent()) {
                return sessionRepository.save(sessionDtoConverter.createSessionDtoToSession(createSessionDto, hall.get(), film.get()));
            } else {
                throw new HallNotFoundException("Hall not found");
            }
        } else {
            throw new FilmNotFoundException("Film not found");
        }
    }

    @Override
    public Session edit(UUID sessionUuid, CreateSessionDto createSessionDto) {

        Session session = findById(sessionUuid).orElseThrow(() -> new IllegalArgumentException("Session not found"));

        Optional<Film> film = filmRepository.findById(createSessionDto.getFilmUuid());
        Optional<Hall> hall = hallRepository.findById(createSessionDto.getHallUuid());
        if(film.isPresent()) {
            if(hall.isPresent()) {
                session.setSessionDate(createSessionDto.getSessionDate());
                session.setActive(createSessionDto.isActive());
                session.setFilm(film.get());
                session.setHall(hall.get());
            } else {
                throw new HallNotFoundException("Hall not found");
            }
        } else {
            throw new FilmNotFoundException("Film not found");
        }
        return sessionRepository.save(session);
    }

    @Override
    public Page<GetSessionDto> findSessionsByFilmId(UUID filmUuid, Pageable pageable) {
        return sessionRepository.getSessionsByFilmId(filmUuid, pageable);
    }

    @Override
    public Page<GetSessionDto> getAllSessions(Pageable pageable) {
        return sessionRepository.findAllSessions(pageable);
    }

    @Override
    public void deleteAllSessionsByFilmId(UUID filmUuid) {
        sessionRepository.getSessionsByFilmIdList(filmUuid).forEach(session -> deleteById(session.getUuid()));
    }

    @Override
    public boolean isOccupied(UUID sessionUuid, int row, int column) {
        Optional<Session> sessionBuscada = sessionRepository.findById(sessionUuid);
        if(sessionBuscada.isPresent()){
            if(sessionBuscada.get().getAvailableSeats()[row][column].equals("O"))
                return true;
            else
                return false;
        }
        else{
            throw new SessionNotFoundException("Sesión no encontrada.");
        }
    }


}
