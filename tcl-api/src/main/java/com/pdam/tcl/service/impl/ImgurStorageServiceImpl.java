package com.pdam.tcl.service.impl;

import com.pdam.tcl.model.img.ImgResponse;
import com.pdam.tcl.model.img.ImgurImg;
import com.pdam.tcl.service.ImgServiceStorage;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImgurStorageServiceImpl implements ImgServiceStorage {
    private static final String URL_POST = "https://api.imgur.com/3/image";


    @Override
    public ImgResponse store(ImgurImg imagen) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Client-ID 55eb55cd14ff95d");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<ImgurImg> imagenEntity = new HttpEntity<>(imagen, headers);

        ImgResponse respuesta = restTemplate.postForObject(URL_POST, imagenEntity, ImgResponse.class);

        if (respuesta != null) {
            if (respuesta.getStatus() == 200) {
                return respuesta;
            }
        }

        return null;
    }

    @Override
    public void delete(UUID deleteId) {

    }

}
