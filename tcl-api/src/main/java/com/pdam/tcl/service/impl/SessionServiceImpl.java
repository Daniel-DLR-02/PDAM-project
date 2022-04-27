package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.CreateSessionDto;
import com.pdam.tcl.model.dto.session.GetSessionDto;
import com.pdam.tcl.repository.SessionRepository;
import com.pdam.tcl.service.FilmService;
import com.pdam.tcl.service.HallService;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.utils.converters.SessionDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SessionDtoConverter sessionDtoConverter;
    private final FilmService filmService;
    private final HallService hallService;

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
        sessionRepository.deleteById(sessionUuid);
    }

    @Override
    public Session save(CreateSessionDto createSessionDto) {
        return sessionRepository.save(sessionDtoConverter.createSessionDtoToSession(createSessionDto));
    }

    @Override
    public Session edit(UUID sessionUuid, CreateSessionDto createSessionDto) {

        Session session = findById(sessionUuid).orElseThrow(() -> new IllegalArgumentException("Session not found"));

        session.setSessionDate(createSessionDto.getSessionDate());
        session.setActive(createSessionDto.isActive());
        session.setAvailableSeats(createSessionDto.getAvailableSeats());
        session.setFilm(filmService.getFilm(createSessionDto.getFilmUuid()));
        session.setHall(hallService.findById(createSessionDto.getHallUuid()));

        return sessionRepository.save(session);
    }

    @Override
    public Page<GetSessionDto> findSessionsByFilmId(UUID filmUuid, Pageable pageable) {
        return sessionRepository.getSessionsByFilmId(filmUuid, pageable);
    }


}
