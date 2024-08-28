package chatapp.carolineeklund.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] AUTH_WHITELIST = {
//            "/auth/signup",// Uppdatera denna rad från "/auth/register"
//            "/auth/login",
            "/api/user",
            "api/login",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui",
            "/swagger-ui/",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers(AUTH_WHITELIST).permitAll();
            auth.anyRequest().authenticated();
        });

        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // Skapar en ny instans av CorsConfiguration.
        CorsConfiguration configuration = new CorsConfiguration();

        // Anger tillåtna ursprung för CORS.
        configuration.setAllowedOrigins(List.of("http://localhost:8005"));
        // Anger tillåtna HTTP-metoder för CORS.
        configuration.setAllowedMethods(List.of("GET", "POST"));
        // Anger tillåtna headers för CORS.
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Skapar en ny instans av UrlBasedCorsConfigurationSource.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Registrerar CORS-konfigurationen för alla URL-mönster.
        source.registerCorsConfiguration("/**", configuration);

        // Returnerar den konfigurerade CorsConfigurationSource.
        return source;
    }
}


