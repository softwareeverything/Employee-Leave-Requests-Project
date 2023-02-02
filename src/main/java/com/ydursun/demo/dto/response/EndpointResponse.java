package com.ydursun.demo.dto.response;

import com.ydursun.demo.dto.BaseDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.Instant;

public class EndpointResponse<T extends BaseDto> implements Serializable {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant timestamp = Instant.now();

    private int status = 200;
    private String error;
    private String path; // /auth/hello

    private T data;

    public EndpointResponse(int status, String error, String path, T data) {
        this.status = status;
        this.error = error;
        this.path = path;
        this.data = data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
