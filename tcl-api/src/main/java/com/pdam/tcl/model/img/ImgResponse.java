package com.pdam.tcl.model.img;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
@Builder
public class ImgResponse {

    private ImgurImageInfo data;
    private String success;
    private int status;

    public ImgResponse() { }

    public ImgResponse(ImgurImageInfo data, String success, int status) {
        super();
        this.data = data;
        this.success = success;
        this.status = status;
    }

    public ImgurImageInfo getData() {
        return data;
    }

    public void setData(ImgurImageInfo data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RespuestaImagen [data=" + data + ", success=" + success + ", status=" + status + "]";
    }





}
