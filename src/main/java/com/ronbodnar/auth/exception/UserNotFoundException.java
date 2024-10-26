package com.ronbodnar.auth.exception;

/**
 * Exception thrown when a user already exists in the system.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with a default message.
     */
    public UserNotFoundException() {
        super("User with the provided details already exists.");
    }

    /**
     * Constructs a new UserNotFoundException with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "UserNotFoundException{" +
                "message='" + getMessage() + '\'' +
                '}';
    }
}