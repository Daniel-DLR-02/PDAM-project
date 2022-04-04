package com.pdam.tcl.service.impl;

import com.pdam.tcl.config.StorageProperties;
import com.pdam.tcl.errors.exception.FileNotFoundException;
import com.pdam.tcl.errors.exception.StorageException;
import com.pdam.tcl.service.StorageService;
import com.pdam.tcl.utils.MediaTypeUrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
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

        return newFilename;

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

        MediaTypeUrlUtil mediaTypeUrlResource = new MediaTypeUrlUtil(filePath.toUri());

        // Instancio como mediaTypeResource el path que me pasan por parámetro
        // para comprobar si és válido y si no lanzar una excepción.
        try {
            if (mediaTypeUrlResource.exists() || mediaTypeUrlResource.isReadable()) {
                Files.delete(filePath);
            } else {
                throw new FileNotFoundException(
                        "No se ha podido leer el archivo: " + filePath);
            }
        }catch (MalformedURLException e) {
            throw new FileNotFoundException("No se ha podido leer el archivo: " + filePath, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
