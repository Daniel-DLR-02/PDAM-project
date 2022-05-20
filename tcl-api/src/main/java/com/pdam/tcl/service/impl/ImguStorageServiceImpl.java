package com.pdam.tcl.service.impl;

import com.pdam.tcl.service.ImgServiceStorage;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImguStorageServiceImpl implements ImgServiceStorage {
    @Override
    public Optional<String> store(MultipartFile file) throws IOException {
        if(!file.isEmpty()){
            byte[] image = Base64.encodeBase64(file.getBytes());
            file.
        }else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> loadAsResource(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<String> delete(UUID deleteId) {
        return Optional.empty();
    }
}
