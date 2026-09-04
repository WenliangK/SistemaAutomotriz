package com.autogestion.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * CORS configuration shared by both the global CorsFilter and Spring Security.
     * - Allows localhost:5500 as origin
     * - All standard methods including OPTIONS
     * - All headers (Authorization, Content-Type, etc.)
     * - Credentials enabled for JWT via Authorization header
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "http://localhost:5500",
                "http://localhost:*"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L); // cache preflight for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Global CorsFilter that runs BEFORE the Spring Security filter chain.
     *
     * Why this is needed:
     *   Spring Security's .cors() adds a CorsFilter INSIDE the security chain,
     *   but JwtAuthFilter (OncePerRequestFilter) runs before it. On OPTIONS
     *   preflight requests there is no Authorization header, yet the JWT filter
     *   still executes and can interfere with the response. By registering this
     *   bean with HIGHEST_PRECEDENCE, CORS preflights are short-circuited
     *   immediately — they never reach Spring Security at all.
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
                new CorsFilter(corsConfigurationSource())
        );
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers("/h2-console/**").permitAll()

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/productos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")

                .requestMatchers("/api/inventario/**").hasAnyRole("ADMIN", "ALMACENERO")

                .requestMatchers("/api/recepciones/**").hasAnyRole("ADMIN", "MECANICO")

                .requestMatchers("/api/diagnosticos/**").hasAnyRole("ADMIN", "MECANICO")

                .requestMatchers("/api/cotizaciones/**").hasAnyRole("ADMIN", "MECANICO")

                .requestMatchers("/api/ordenes-trabajo/**").hasAnyRole("ADMIN", "MECANICO")

                .requestMatchers("/api/pagos/**").hasRole("ADMIN")
                .requestMatchers("/api/entregas/**").hasRole("ADMIN")

                .requestMatchers("/api/reportes/**").authenticated()

                .requestMatchers(HttpMethod.GET, "/api/productos").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/clientes").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/vehiculos/**").authenticated()

                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
