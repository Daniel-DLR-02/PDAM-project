package com.pdam.tcl.service.impl;

import com.pdam.tcl.service.FileManagerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class FileManagerServiceImpl implements FileManagerService {


    @PostConstruct
    @Override
    public void init() {
        /*try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }*/
    }

    @Override
    public String store(MultipartFile file) throws IOException, Exception {
        return null;
    }

    @Override
    public String createUri(String fileName) {
        return null;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void deleteFile(Path filePath) throws IOException {

    }

    @Override
    public void deleteAll() {

    }
}
