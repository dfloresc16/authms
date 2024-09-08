package com.pt.authms.model.commons;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonResponse implements Serializable {


    @JsonProperty("success")
    private boolean success;

    @JsonProperty("httpstatus")
    private Integer httpstatus;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

    @JsonProperty("message")
    private String message;

    public CommonResponse(boolean success, Integer httpstatus, String errorCode, String errorMessage, String message) {
        this.success = success;
        this.httpstatus = httpstatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.message = message;
    }

}
