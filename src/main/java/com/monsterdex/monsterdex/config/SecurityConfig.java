package com.monsterdex.monsterdex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Desenvolvimento: sem login, tudo liberado.
        // Futuro: habilitar autenticação (formLogin/httpBasic) e regras de autorização por rota.
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // Necessário para permitir frames do H2 Console
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(form -> form.disable())
            .logout(logout -> logout.disable());

        return http.build();
    }

    // Evita a criação do usuário padrão e a mensagem de senha gerada em logs
    @Bean
    UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }
}
