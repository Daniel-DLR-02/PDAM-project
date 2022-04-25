package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.Session;
import com.pdam.tcl.repository.SessionRepository;
import com.pdam.tcl.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public boolean existsByid(UUID id) {
        return sessionRepository.existsById(id);
    }

    @Override
    public Optional<Session> findById(UUID sessionUuid) {
        return sessionRepository.findById(sessionUuid);
    }
}
