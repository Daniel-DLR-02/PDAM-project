package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.Session;
import com.pdam.tcl.model.dto.session.CreateSessionDto;
import com.pdam.tcl.repository.SessionRepository;
import com.pdam.tcl.service.SessionService;
import com.pdam.tcl.utils.converters.SessionDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SessionDtoConverter sessionDtoConverter;

    @Override
    public boolean existsByid(UUID id) {
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
}
