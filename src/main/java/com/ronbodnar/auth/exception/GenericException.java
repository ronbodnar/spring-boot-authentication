package com.ronbodnar.auth.exception;

/**
 * Exception thrown when a user already exists in the system.
 */
public class GenericException extends RuntimeException {

    /**
     * Constructs a new GenericException with a default message.
     */
    public GenericException() {
        super("User with the provided details already exists.");
    }

    /**
     * Constructs a new GenericException with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public GenericException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "GenericException{" +
                "message='" + getMessage() + '\'' +
                '}';
    }
}