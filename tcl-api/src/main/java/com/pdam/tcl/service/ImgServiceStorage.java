package com.pdam.tcl.service;

import com.pdam.tcl.model.img.ImgResponse;
import com.pdam.tcl.model.img.ImgurImg;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface ImgServiceStorage {
    ImgResponse store(ImgurImg imagen);
    void delete(String deleteId);
}
