package com.pt.authms.model.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pt.authms.model.commons.CommonResponse;

public class GenericResponseDTO<T> extends CommonResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("body")
    private T body;

    /**
     * Default constructor.
     *
     * @param success      The success flag.
     * @param httpStatus   The http status code.
     * @param errorCode    The error code.
     * @param errorMessage An error message.
     * @param message      A message.
     * @param body         The body of the response.
     */
    public GenericResponseDTO(boolean success, Integer httpStatus, String errorCode, String errorMessage,
                              String message, T body) {
        super(success, httpStatus, errorCode, errorMessage, message);
        this.body = body;
    }

}