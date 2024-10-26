package com.ronbodnar.auth.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A class representing an error response with a status and message.
 */
@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;

    /**
     * Constructor for creating an ErrorResponse with a message and status.
     *
     * @param message the error message
     * @param status  the HTTP status code
     */
    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}