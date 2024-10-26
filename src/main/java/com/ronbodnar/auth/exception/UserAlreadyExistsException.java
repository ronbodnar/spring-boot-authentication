package com.ronbodnar.auth.exception;

/**
 * Exception thrown when a user already exists in the system.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with a default message.
     */
    public UserAlreadyExistsException() {
        super("User with the provided details already exists.");
    }

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "UserAlreadyExistsException{" +
                "message='" + getMessage() + '\'' +
                '}';
    }
}