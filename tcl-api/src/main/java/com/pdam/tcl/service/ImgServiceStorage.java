package com.pdam.tcl.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface ImgServiceStorage {
    Optional<String> store(MultipartFile file) throws IOException;
    Optional<String> loadAsResource(UUID id);
    Optional<String> delete(UUID deleteId);
}
