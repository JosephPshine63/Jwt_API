package dev.pioruocco.config;

import dev.pioruocco.filter.JwtAuthenticationFilter;
import dev.pioruocco.model.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //configuring the security filter chain
    /*
    It configures the security chain using the provided HttpSecurity object.
        1. csrf().disable(): Disables CSRF protection (Cross-Site Request Forgery)
         as it might not be required for your specific API-driven application
         (potentially vulnerable in web applications).

        2. authorizeHttpRequests(req -> ...): Configures authorization rules:
            -> Grants access to URLs matching "login/" and "register/"
               without requiring authentication (.permitAll()).
            -> Requires authentication (.authenticated()) for any other request.

        3. userDetailsService(userDetailsService): Sets the custom user details service
           to be used for user lookup during authentication.

        4. sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)):
           Disables session management as JWT is a stateless authentication mechanism.

        5. addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class):
           Adds the "jwtAuthenticationFilter" before the default "UsernamePasswordAuthenticationFilter".
           This ensures JWT processing happens before attempting username/password authentication.

    The method returns the configured SecurityFilterChain bean.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req-> req.requestMatchers("login/**", "/register/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                ).userDetailsService(userDetailsService)
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    //creating the password encoder
    /*
    This method creates and returns a Spring bean of type PasswordEncoder.

    It uses the BCryptPasswordEncoder class, a popular choice for password hashing due to its work
    factor and security features.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //creating the AuthenticationManager
    /*
    This method creates and returns a Spring bean of type AuthenticationManager.

    It retrieves the AuthenticationManager from the provided AuthenticationConfiguration object.
    This manager is responsible for user authentication using configured providers.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
