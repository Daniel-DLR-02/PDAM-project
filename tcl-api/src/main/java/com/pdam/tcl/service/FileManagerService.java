package com.pdam.tcl.service;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileManagerService {

    void init();

    String store(MultipartFile file) throws IOException, Exception;

    String createUri(String fileName);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteFile(Path filePath) throws IOException;

    void deleteAll();

}
