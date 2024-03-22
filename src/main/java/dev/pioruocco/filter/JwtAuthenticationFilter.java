package dev.pioruocco.filter;

import dev.pioruocco.model.service.JwtService;
import dev.pioruocco.model.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    //
    /*
    This method is the core of the filter and is called whenever a request passes through the filter.
    It overrides the doFilterInternal method from the OncePerRequestFilter class of Spring Security.
    Here's a breakdown of the logic:
        1. It retrieves the Authorization header from the request.

        2. If the header is missing or doesn't start with "Bearer ",
           it allows the request to proceed without further processing
           (filterChain.doFilter(request, response)) as it likely doesn't contain a JWT token for authentication.

        3. If a valid "Bearer " header is found, it extracts the token string from the header.

        4. It calls the jwtService's extractUsername method to retrieve the username from the token.

        5. If a username is extracted and there's no existing authentication in the SecurityContext:
            -> It loads the user details object using the userDetailsService's loadUserByUsername method.
            -> It calls the jwtService's isValid method to verify the token for the retrieved user.

        6. If the token is valid:
            -> A UsernamePasswordAuthenticationToken object is created with the loaded
               user details, null password (as JWT doesn't provide password), and user authorities.
            -> Details about the request are set on the authentication token using WebAuthenticationDetailsSource
            .
    The SecurityContext is set with this authentication object, indicating a
    successful JWT-based authentication.
    Finally, it allows the request to proceed through the
    filter chain(filterChain.doFilter(request, response)) with the established security context.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtService.isValid(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request, response);
            }
        }
    }
}
