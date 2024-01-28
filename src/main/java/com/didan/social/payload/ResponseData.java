package com.didan.social.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseData {
    private int statusCode = 200;
    private boolean isSuccess = true;
    private String description;
    private Object data;

    public ResponseData() {
    }

    @JsonProperty(index = 0)
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @JsonProperty(index = 2)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty(index = 1)
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty(index = 3)
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
