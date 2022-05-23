package com.pdam.tcl.utils.converters;

import com.pdam.tcl.errors.exception.FilmNotFoundException;
import com.pdam.tcl.errors.exception.HallNotFoundException;
import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.CreateSessionDto;
import com.pdam.tcl.model.dto.session.GetSessionDto;
import com.pdam.tcl.service.FilmService;
import com.pdam.tcl.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionDtoConverter {

    private final FilmService filmService;
    private final HallService hallService;

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


    public Session createSessionDtoToSession(CreateSessionDto createSessionDto) {
        return Session.builder()
                .film(filmService.findById(createSessionDto.getFilmUuid()).orElseThrow(()-> new FilmNotFoundException("Film not found")) )
                .hall(hallService.findById(createSessionDto.getHallUuid()))
                .sessionDate(createSessionDto.getSessionDate())
                .active(createSessionDto.isActive())
                .availableSeats(hallService.findById(createSessionDto.getHallUuid()).getSeats())
                .build();
    }
}
