package com.ronbodnar.auth.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.ronbodnar.auth.exception.GenericException;
import com.ronbodnar.auth.user.User;
import com.ronbodnar.auth.user.UserRepository;

/**
 * Custom AuthenticationProvider for authenticating users against a database.
 * Implements Spring Security's AuthenticationProvider interface.
 */
@Component
public class DatabaseAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    /**
     * Constructs a new DatabaseAuthenticationProvider.
     *
     * @param userRepository the UserRepository used to fetch user details from the
     *                       database
     */
    public DatabaseAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticates the provided authentication token against the database.
     *
     * @param authentication the authentication request object containing the user's
     *                       credentials
     * @return an Authentication object if authentication is successful
     * @throws AuthenticationException if authentication fails
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new GenericException());

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(user, password);
    }

    /**
     * Checks if this provider supports the specified authentication type.
     *
     * @param authentication the authentication class to check
     * @return true if this provider supports the authentication type; false
     *         otherwise
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
