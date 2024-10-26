package com.ronbodnar.auth.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A custom handler that processes access denied exceptions.
 */
@Component
public class AccessDeniedResponseHandler implements AccessDeniedHandler {

    final Logger logger = LoggerFactory.getLogger(AccessDeniedResponseHandler.class);

    /**
     * Handles the access denied exception by logging the error and sending a JSON
     * response
     * to the client with the status and error message.
     *
     * @param request               the incoming HTTP request
     * @param response              the outgoing HTTP response
     * @param accessDeniedException the exception that indicates access is denied
     * @throws IOException      if an I/O error occurs during the handling of the
     *                          response
     * @throws ServletException if a servlet-related error occurs
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.error("Authorization error: {}", accessDeniedException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("payload", accessDeniedException.getMessage());

        // Write the response body as JSON
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
