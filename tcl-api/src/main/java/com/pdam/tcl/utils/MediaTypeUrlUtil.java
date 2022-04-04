package com.pdam.tcl.utils;

import com.pdam.tcl.errors.exception.StorageException;
import org.apache.tika.Tika;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MediaTypeUrlUtil extends UrlResource {

    public MediaTypeUrlUtil(URI uri) throws MalformedURLException {
        super(uri);
    }

    public MediaTypeUrlUtil(URL url) {
        super(url);
    }

    public MediaTypeUrlUtil(String path) throws MalformedURLException {
        super(path);
    }

    public MediaTypeUrlUtil(String protocol, String location) throws MalformedURLException {
        super(protocol, location);
    }

    public MediaTypeUrlUtil(String protocol, String location, String fragment) throws MalformedURLException {
        super(protocol, location, fragment);
    }

    public String getType() {
        Tika tika = new Tika();
        try {
            return tika.detect(this.getFile());
        } catch (IOException e) {
            throw new StorageException("Error obteniendo el tipo MIME del fichero", e);
        }
    }

}