package com.brincadeiras.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/actuator/**" ).permitAll() // Permite acesso a todos os endpoints do Actuator
                                .anyRequest().permitAll() // Permite acesso a todos os outros endpoints (seus controllers)
                )
                .csrf(csrf -> csrf.disable()); // Desabilita CSRF, comum para APIs REST

        return http.build( );
    }
}