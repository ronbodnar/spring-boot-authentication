package com.ronbodnar.auth.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check
     * @return true if a user with the given email exists, otherwise false
     */
    Boolean existsByEmail(String email);

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the found user, or empty if no user found
     */
    Optional<User> findByUsername(String username);

}
