package com.ronbodnar.auth.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A custom entry point for handling unauthorized access attempts in a Spring
 * Security context.
 */
@Component
public class UnauthorizedAccessEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(UnauthorizedAccessEntryPoint.class);

    /**
     * Commences the authentication process when an unauthenticated user tries to
     * access a protected resource.
     * Logs the authentication error and sends a JSON response with the error
     * details.
     *
     * @param request       the incoming HTTP request
     * @param response      the outgoing HTTP response
     * @param authException the exception that indicates an authentication error
     * @throws IOException if an I/O error occurs during the handling of the
     *                     response
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        logger.error("Authentication error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("payload", authException.getMessage());

        // Write the response body as JSON
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
