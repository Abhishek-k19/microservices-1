package com.example.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager users() {

        UserDetails user = User.withUsername("user")
                .password("{noop}user123")
                .roles("USER")
                .build();

        UserDetails manager = User.withUsername("manager")
                .password("{noop}manager123")
                .roles("MANAGER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(
                user,
                manager,
                admin
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // ADMIN only
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")

                // MANAGER and ADMIN
                .requestMatchers("/api/manager/**")
                .hasAnyRole("MANAGER", "ADMIN")
                
                .requestMatchers("/api/orders/admin-test")
                .hasRole("ADMIN")

                // All Order APIs require authentication
                .requestMatchers("/api/orders/**")
                .authenticated()

                .anyRequest()
                .permitAll()
            )

            .httpBasic(
                org.springframework.security.config.Customizer
                    .withDefaults()
            );

        return http.build();
    }
}