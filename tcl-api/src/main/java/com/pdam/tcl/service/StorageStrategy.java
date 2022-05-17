package com.pdam.tcl.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public interface StorageStrategy {

    String upload(MultipartFile multipartFile) throws IOException;

    ResponseEntity<Object> download(String fileName, HttpServletRequest request) throws Exception;


}
