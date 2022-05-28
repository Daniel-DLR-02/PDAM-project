package com.pdam.tcl.utils.converters;

import com.pdam.tcl.errors.exception.FilmNotFoundException;
import com.pdam.tcl.errors.exception.HallNotFoundException;
import com.pdam.tcl.model.Film;
import com.pdam.tcl.model.Hall;
import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.CreateSessionDto;
import com.pdam.tcl.model.dto.session.GetSessionDataDto;
import com.pdam.tcl.model.dto.session.GetSessionDto;
import com.pdam.tcl.repository.FilmRepository;
import com.pdam.tcl.repository.HallRepository;
import com.pdam.tcl.service.FilmService;
import com.pdam.tcl.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionDtoConverter {

    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public GetSessionDto sessionToGetSessionDto(Session session) {
        return GetSessionDto.builder()
                .sessionId(session.getUuid())
                .filmTitle(session.getFilm().getTitle())
                .sessionDate(session.getSessionDate())
                .hallName(session.getHall().getName())
                .active(session.isActive())
                .availableSeats(session.getAvailableSeats())
                .build();

    }

    public GetSessionDataDto sessionToGetSessionDataDto(Session session) {
        return GetSessionDataDto.builder()
                .sessionId(session.getUuid())
                .filmTitle(session.getFilm().getTitle())
                .sessionDate(session.getSessionDate())
                .hallName(session.getHall().getName())
                .build();

    }


    public Session createSessionDtoToSession(CreateSessionDto createSessionDto, Hall hall, Film film) {
        return Session.builder()
                .film(film)
                .hall(hall)
                .sessionDate(createSessionDto.getSessionDate())
                .active(createSessionDto.isActive())
                .availableSeats(hall.getSeats())
                .build();
    }
}
