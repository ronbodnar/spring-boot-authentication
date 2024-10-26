package com.ronbodnar.auth.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A generic class to represent a successful response.
 *
 * @param <T> the type of the data returned in the response
 */
@Getter
@Setter
@NoArgsConstructor
public class SuccessResponse<T> {

    private T data;
    private String message;
    private int status;

    /**
     * Constructor for creating a SuccessResponse with data.
     *
     * @param data   the data to include in the response
     * @param status the HTTP status code
     */
    public SuccessResponse(T data, int status) {
        this.data = data;
        this.status = status;
    }

    /**
     * Constructor for creating a SuccessResponse with just a message.
     *
     * @param message the message to include in the response
     * @param status  the HTTP status code
     */
    public SuccessResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}