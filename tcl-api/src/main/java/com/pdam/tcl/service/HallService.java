package com.pdam.tcl.service;

import com.pdam.tcl.model.Hall;
import com.pdam.tcl.model.dto.hall.CreateHallDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HallService {

    Hall findById(UUID id);

    Page<Hall> findAll(Pageable pageable);

    Hall findOne(UUID id);

    Hall save(CreateHallDto createHallDto);

    void delete(UUID id);

    Hall edit(UUID id, CreateHallDto createHallDto);

}
