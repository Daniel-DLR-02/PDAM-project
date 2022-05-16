package com.pdam.tcl.service.impl;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.pdam.tcl.config.StorageProperties;
import com.pdam.tcl.errors.exception.FileNotFoundException;
import com.pdam.tcl.errors.exception.StorageException;
import com.pdam.tcl.service.StorageService;
import com.pdam.tcl.utils.MediaTypeUrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class FileManagerServiceImpl implements StorageService {


    private final Path rootLocation;

    @Autowired
    public FileManagerServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) throws IOException {

        String filename = StringUtils.cleanPath(file.getOriginalFilename().replace(" ","_"));
        String newFilename = "";

        try {
            if (file.isEmpty())
                throw new StorageException("El fichero subido está vacío");

            newFilename = filename;
            while(Files.exists(rootLocation.resolve(newFilename))) {
                String extension = StringUtils.getFilenameExtension(newFilename);
                String name = newFilename.replace("."+extension,"");

                String suffix = Long.toString(System.currentTimeMillis());
                suffix = suffix.substring(suffix.length()-6);
                newFilename = name + "_" + suffix + "." + extension;

            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, rootLocation.resolve(newFilename),
                        StandardCopyOption.REPLACE_EXISTING);
            }



        } catch (IOException ex) {
            throw new StorageException("Error en el almacenamiento del fichero: " + newFilename, ex);
        }

        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setProjectId("my-project")
                .setCredentials(GoogleCredentials
                        .fromStream(new ClassPathResource("firebase.json").getInputStream()))
                .build();
        Storage storage = storageOptions.getService();
        BlobId blobId = BlobId.of("bucket-name", filename);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        Blob blob = storage.create(blobInfo, file.getBytes());

        System.out.println("UPLOAD FILE" + file.getName() + " " + file.getSize() + " octet");
        return "https://storage.cloud.google.com/tcl-bucket"+filename;
    }

    private String createFileName(String filename) {
        String newFilename = filename;
        while(Files.exists(rootLocation.resolve(newFilename))) {
            String extension = StringUtils.getFilenameExtension(newFilename);
            String name = newFilename.replace("."+extension,"");

            String suffix = Long.toString(System.currentTimeMillis());
            suffix = suffix.substring(suffix.length()-6);

            newFilename = name + "_" + suffix + "." + extension;

        }

        return newFilename;
    }

    @Override
    public String createUri(String fileName){
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(fileName)
                .toUriString();
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Error al leer los ficheros almacenados", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {

        try {
            Path file = load(filename);
            MediaTypeUrlUtil resource = new MediaTypeUrlUtil(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new FileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteFile(Path filePath) throws IOException {

        try {
            // Delete file or directory
            Files.delete(filePath);
            System.out.println("File or directory deleted successfully");
        } catch (NoSuchFileException ex) {
            System.out.printf("No such file or directory: %s\n", filePath);
        } catch (DirectoryNotEmptyException ex) {
            System.out.printf("Directory %s is not empty\n", filePath);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }



}
