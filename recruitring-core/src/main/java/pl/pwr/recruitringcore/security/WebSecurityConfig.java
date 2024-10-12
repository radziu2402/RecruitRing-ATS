package pl.pwr.recruitringcore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.pwr.recruitringcore.model.enums.Role;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reset-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reset-password/confirm").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reset-password/verify-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/verify-email").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/confirm-code").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/public-jobs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/public-jobs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/apply").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/locations/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/job-categories/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/titles/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/register").hasAuthority(Role.ADMINISTRATOR.toString())
                        .anyRequest().authenticated());
        return http.build();
    }

}