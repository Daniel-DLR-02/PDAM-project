package com.pdam.tcl.model.img;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgurImg {

    private String title;
    private String description;
    private String image;
    private String name;

    public ImgurImg() { }

    /**
     * Constructor "preferible"
     * @param image Imagen codificada como una cadena de caracteres en base64
     * @param name Nombre del fichero
     */
    public ImgurImg(String image, String name) {
        super();
        this.image = image;
        this.name = name;
    }

    /**
     * Constructor completo
     *
     * @param title Título de la imagen
     * @param description Descripción de la imagen
     * @param image Imagen codificada como una cadena de caracteres en base64
     * @param name Nombre del fichero
     */
    public ImgurImg(String title, String description, String image, String name) {
        super();
        this.title = title;
        this.description = description;
        this.image = image;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Imagen [title=" + title + ", description=" + description + ", image=" + image + ", name=" + name + "]";
    }



}