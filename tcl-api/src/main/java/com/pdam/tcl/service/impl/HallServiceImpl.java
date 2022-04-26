package com.pdam.tcl.service.impl;

import com.pdam.tcl.errors.exception.HallNotFoundException;
import com.pdam.tcl.model.Hall;
import com.pdam.tcl.repository.HallRepository;
import com.pdam.tcl.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;

    @Override
    public Hall findById(UUID id) {
        return hallRepository.findById(id).orElseThrow(() -> new HallNotFoundException("Hall not found"));
    }
}
