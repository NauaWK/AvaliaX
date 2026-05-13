package dev.trabalho.xfragil.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/usuarios/**").hasRole("ADMIN")
                    .requestMatchers("/pacientes/**").hasAnyRole("ADMIN", "USER")

                    .requestMatchers(HttpMethod.POST, "/avaliacoes/**").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, "/avaliacoes/**").hasAnyRole("ADMIN", "USER")
                    .requestMatchers(HttpMethod.PATCH, "/avaliacoes/**").hasAnyRole("ADMIN", "USER")
                    .requestMatchers(HttpMethod.DELETE, "/avaliacoes/**").hasAnyRole("ADMIN", "USER")

                    .requestMatchers("/relatorios/**").hasAnyRole("ADMIN", "USER")
                    .anyRequest().authenticated()
            )

            .formLogin(Customizer.withDefaults())
            .logout(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
