package com.moviehouse.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;

    private final CustomAuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    private static final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/auth/**",
            "/api/movies/**",
            "/api/sessions/**",
            "/api/halls/**",
            "/api/bookings/**",
            "/api/payments/**",
            "/api/tickets/**"
    };

    private static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/auth/**",
            "/api/bookings/**",
            "/api/payments/**"
    };

    private static final String[] ADMIN_ENDPOINTS = {
            "/api/auth/**",
            "/api/users/**",
            "/api/movies/**",
            "/api/sessions/**",
            "/api/halls/**",
            "/api/bookings/**",
            "/api/payments/**",
            "/api/tickets/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()

                        .requestMatchers(ADMIN_ENDPOINTS).hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionHandlerFilter, JwtAuthFilter.class)
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .build();
    }
}