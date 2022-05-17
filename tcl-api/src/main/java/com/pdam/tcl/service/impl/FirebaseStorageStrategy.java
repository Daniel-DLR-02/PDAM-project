package com.pdam.tcl.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import com.pdam.tcl.service.StorageStrategy;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;

@Service
public class FirebaseStorageStrategy implements StorageStrategy {
    private final Logger log = LoggerFactory.getLogger(FirebaseStorageStrategy.class);
    private final Environment environment;
    private StorageOptions storageOptions;
    private String bucketName;
    private String projectId;

    public FirebaseStorageStrategy(Environment environment) {
        this.environment = environment;
    }


    @PostConstruct
    private void initializeFirebase() throws Exception {
        bucketName = environment.getRequiredProperty("FIREBASE_BUCKET_NAME");
        projectId = environment.getRequiredProperty("FIREBASE_PROJECT_ID");
        FileInputStream serviceAccount =
                new FileInputStream("/home/user/Downloads/service-account-file.json");

        this.storageOptions = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

    }

    @Override
    public String upload(MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        Path filePath = file.toPath();
        String objectName = generateFileName(multipartFile);

        Storage storage = storageOptions.getService();

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob blob = storage.create(blobInfo, Files.readAllBytes(filePath));

        log.info("File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
        return objectName;
    }


    @Override
    public ResponseEntity<Object> download(String fileName, HttpServletRequest request) throws Exception {
        Storage storage = storageOptions.getService();

        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        ReadChannel reader = blob.reader();
        InputStream inputStream = Channels.newInputStream(reader);

        byte[] content = null;
        log.info("File downloaded successfully.");

        content = IOUtils.toByteArray(inputStream);

        final ByteArrayResource byteArrayResource = new ByteArrayResource(content);

        return ResponseEntity
                .ok()
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(byteArrayResource);

    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

}
