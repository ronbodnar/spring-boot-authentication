package com.ronbodnar.auth.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ronbodnar.auth.security.service.DatabaseUserDetailsService;
import com.ronbodnar.auth.security.service.JwtService;

/**
 * A filter that processes JWT authentication for incoming HTTP requests.
 * This filter extracts the JWT from cookies, validates it, and sets the
 * authentication in the security context if the token is valid.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private DatabaseUserDetailsService userDetailsService;

    /**
     * Sets the JWT service to be used for handling JWT operations.
     *
     * @param jwtService the service responsible for JWT operations
     */
    @Autowired
    public void setJwtUtils(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Sets the user details service to be used for loading user data.
     *
     * @param userDetailsService the service to load user-specific data
     */
    @Autowired
    public void setUserDetailsService(DatabaseUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests to extract and validate the JWT from cookies.
     * If the JWT is valid, it retrieves the username and sets the authentication
     * in the security context.
     *
     * @param request     the incoming HTTP request
     * @param response    the outgoing HTTP response
     * @param filterChain the filter chain for further processing of the request
     * @throws ServletException if an error occurs during filtering
     * @throws IOException      if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtService.getFromCookies(request);
            if (token != null && jwtService.validate(token)) {
                String username = jwtService.getUsername(token);
                // Load user details using the username from the token
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't set JWT user authentication: {}".formatted(e));
        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
