package com.pdam.tcl.service.impl;

import com.pdam.tcl.errors.exception.HallNotFoundException;
import com.pdam.tcl.model.Hall;
import com.pdam.tcl.model.dto.hall.CreateHallDto;
import com.pdam.tcl.repository.HallRepository;
import com.pdam.tcl.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;

    private static final String[][] HALL_SEATS = {
            {"S","S","S","S","P","P","S","S","S","S","S","S","S","S","P","P","S","S","S","S"},
            {"S","S","S","S","P","P","S","S","S","S","S","S","S","S","P","P","S","S","S","S"},
            {"S","S","S","S","P","P","S","S","S","S","S","S","S","S","P","P","S","S","S","S"},
            {"S","S","S","S","P","P","S","S","S","S","S","S","S","S","P","P","S","S","S","S"},
            {"S","S","S","S","P","P","S","S","S","S","S","S","S","S","P","P","S","S","S","S"},
            {"S","S","S","S","P","P","S","S","S","S","S","S","S","S","P","P","S","S","S","S"},
            {"S","S","S","S","P","P","S","S","S","S","S","S","S","S","P","P","S","S","S","S"},
            {"P","P","P","P","P","P","S","S","S","S","S","S","S","S","P","P","P","P","P","P"},
            {"P","P","P","P","P","P","S","S","S","S","S","S","S","S","P","P","P","P","P","P"},
            {"P","P","P","P","P","P","S","S","S","S","S","S","S","S","P","P","P","P","P","P"},
            {"P","P","P","P","P","P","S","S","S","S","S","S","S","S","P","P","P","P","P","P"},
            {"P","P","P","P","P","P","S","S","S","S","S","S","S","S","P","P","P","P","P","P"},};

    @Override
    public Hall findById(UUID id) {
        return hallRepository.findById(id).orElseThrow(() -> new HallNotFoundException("Hall not found"));
    }

    @Override
    public Page<Hall> findAll(Pageable pageable) {
        return hallRepository.findAll(pageable);
    }

    @Override
    public Hall findOne(UUID id) {
        return hallRepository.findById(id).orElseThrow(() -> new HallNotFoundException("Hall not found"));
    }

    @Override
    public Hall save(CreateHallDto createHallDto) {

        return hallRepository.save(Hall.builder()
                        .name(createHallDto.getName())
                        .seats(HALL_SEATS)
                        .build());
    }

    @Override
    public void delete(UUID id) {
        hallRepository.deleteById(id);
    }

    @Override
    public Hall edit(UUID id, CreateHallDto createHallDto) {

        Hall hall = hallRepository.findById(id).orElseThrow(() -> new HallNotFoundException("Hall not found"));

        hall.setName(createHallDto.getName());

        return hallRepository.save(hall);
    }

    @Override
    public boolean existsByName(String name) {
        return hallRepository.existsByName(name);
    }
}
