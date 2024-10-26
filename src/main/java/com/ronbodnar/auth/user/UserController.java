package com.ronbodnar.auth.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing a list of users and HTTP status.
     */
    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users); // Return users in response
    }

    /**
     * Adds a new user.
     *
     * @param user The user to be added.
     * @return ResponseEntity indicating the outcome of the operation.
     */
    @PostMapping("")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            return userService.addUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding user: " + e.getMessage());
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the user or an error message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(ResponseEntity::ok) // Return user if found
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404 if not found
    }
}
