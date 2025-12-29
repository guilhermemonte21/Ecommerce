//package com.github.guilhermemonte21.Ecommerce.Infra.Config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // necessário para POST e H2
//                .csrf(csrf -> csrf.disable())
//
//                // libera o H2 Console
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers("/Usuario/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // permite iframe do H2
//                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
//
//                // mantém basic auth (opcional)
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//}