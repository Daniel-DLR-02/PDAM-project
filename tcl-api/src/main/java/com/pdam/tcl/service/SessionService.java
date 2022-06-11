package com.pdam.tcl.service;

import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.CreateSessionDto;
import com.pdam.tcl.model.dto.session.GetSessionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionService {

    boolean existsById(UUID id);

    Optional<Session> findById(UUID sessionUuid);

    void deleteById(UUID sessionUuid);

    Session save(CreateSessionDto createSessionDto);

    Session edit(UUID sessionUuid, CreateSessionDto createSessionDto);

    Page<GetSessionDto> findSessionsByFilmId(UUID filmUuid, Pageable pageable);

    Page<GetSessionDto> getAllSessions(Pageable pageable);

    void deleteAllSessionsByFilmId(UUID filmUuid);

    boolean isOccupied(UUID sessionUuid,int row,int column);
}
