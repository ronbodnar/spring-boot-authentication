package com.ronbodnar.auth.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ronbodnar.auth.exception.GenericException;
import com.ronbodnar.auth.security.model.UserPrincipal;
import com.ronbodnar.auth.user.User;
import com.ronbodnar.auth.user.UserRepository;

/**
 * Implementation of UserDetailsService that retrieves user details from a
 * database.
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new DatabaseUserDetailsService.
     *
     * @param userRepository the UserRepository used to fetch user details from the
     *                       database
     */
    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username.
     *
     * @param username the username of the user to load
     * @return a UserDetails object containing user information
     * @throws UsernameNotFoundException if the user with the specified username is
     *                                   not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new GenericException());

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        System.out.println("User loaded: ");
        System.out.println(user);

        return UserPrincipal.build(user);
    }
}
