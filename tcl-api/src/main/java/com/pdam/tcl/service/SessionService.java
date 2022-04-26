package com.pdam.tcl.service;

import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.CreateSessionDto;

import java.util.Optional;
import java.util.UUID;

public interface SessionService {

    boolean existsByid(UUID id);

    Optional<Session> findById(UUID sessionUuid);

    void deleteById(UUID sessionUuid);

    Session save(CreateSessionDto createSessionDto);
}
