package com.ronbodnar.auth.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ronbodnar.auth.security.service.DatabaseUserDetailsService;

/**
 * A filter that processes Bearer authentication tokens in HTTP requests.
 * This filter checks for a Bearer token in the Authorization header, validates
 * it, and sets the authentication in the security context if valid.
 */
public class BearerAuthenticationFilter extends OncePerRequestFilter {

    @Value("${com.ronbodnar.auth.security.bearer.token}")
    private String BEARER_TOKEN;

    private DatabaseUserDetailsService userDetailsService;

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
     * Filters incoming requests to extract and validate the Bearer token.
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
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                String token = authHeader.replace("Bearer ", "");
                if (authHeader.startsWith("Bearer ") && token.equals(BEARER_TOKEN)) {
                    // Load user details using the provided username
                    UserDetails userDetails = userDetailsService.loadUserByUsername("test@mail.com");

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.error("Bearer token mismatch");
                }
            }
        } catch (Exception e) {
            logger.error("Can't set Bearer user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }
}
