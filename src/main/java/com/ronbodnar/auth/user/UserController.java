package com.ronbodnar.auth.user;

import com.ronbodnar.auth.payload.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public void getUsers() {
        userRepository.findAll().forEach(System.out::println);
    }

    @PostMapping("")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.ok(new MessageResponse("user exists"));
        }

        userRepository.save(user);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        return user.orElse(null);
    }
}
