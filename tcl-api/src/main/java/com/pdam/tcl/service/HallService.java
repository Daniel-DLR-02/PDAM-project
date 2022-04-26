package com.pdam.tcl.service;

import com.pdam.tcl.model.Hall;

import java.util.UUID;

public interface HallService {

    Hall findById(UUID id);
}
