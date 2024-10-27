package com.ronbodnar.auth.security.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ronbodnar.auth.security.filter.BearerAuthenticationFilter;
import com.ronbodnar.auth.security.filter.JwtAuthenticationFilter;
import com.ronbodnar.auth.security.handler.AccessDeniedResponseHandler;
import com.ronbodnar.auth.security.handler.UnauthorizedAccessEntryPoint;
import com.ronbodnar.auth.security.service.DatabaseUserDetailsService;

/**
 * Security configuration for the User Authentication application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DatabaseUserDetailsService databaseUserDetailsService;
    private final AccessDeniedResponseHandler accessDeniedResponseHandler;
    private final UnauthorizedAccessEntryPoint unauthorizedAccessEntryPoint;

    /**
     * Constructs a SecurityConfig with the specified services.
     *
     * @param unauthorizedAccessEntryPoint the entry point for unauthorized access
     * @param accessDeniedResponseHandler  the handler for access denied responses
     * @param databaseUserDetailsService   the service to load user-specific data
     */
    public SecurityConfig(UnauthorizedAccessEntryPoint unauthorizedAccessEntryPoint,
            AccessDeniedResponseHandler accessDeniedResponseHandler,
            DatabaseUserDetailsService databaseUserDetailsService) {
        this.databaseUserDetailsService = databaseUserDetailsService;
        this.accessDeniedResponseHandler = accessDeniedResponseHandler;
        this.unauthorizedAccessEntryPoint = unauthorizedAccessEntryPoint;
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http the HttpSecurity object used to configure web-based security
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring the security filter
     *                   chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedAccessEntryPoint)
                        .accessDeniedHandler(accessDeniedResponseHandler))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(bearerAuthenticationFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the authentication manager with a DaoAuthenticationProvider.
     *
     * @return the AuthenticationManager for user authentication
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(databaseUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    /**
     * Creates and configures the CorsConfigurationSource bean.
     * 
     * @return the configured CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }

    /**
     * Creates and configures the JwtAuthenticationFilter bean.
     *
     * @return the configured JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Creates and configures the BearerAuthenticationFilter bean.
     *
     * @return the configured BearerAuthenticationFilter
     */
    @Bean
    public BearerAuthenticationFilter bearerAuthenticationFilter() {
        return new BearerAuthenticationFilter();
    }

    /**
     * Configures the BCryptPasswordEncoder bean for password encryption.
     *
     * @return the configured BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
