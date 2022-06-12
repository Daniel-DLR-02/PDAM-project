package com.pdam.tcl.repository;

import com.pdam.tcl.model.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HallRepository extends JpaRepository<Hall, UUID> {

    Page<Hall> findAll(Pageable pageable);

    boolean existsByName(String name);
}
