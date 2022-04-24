package com.pdam.tcl.service;

import com.pdam.tcl.model.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionService {

    boolean existsByid(UUID id);

    Optional<Session> findById(UUID sessionUuid);
}
