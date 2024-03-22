package dev.pioruocco.model.service;

import dev.pioruocco.model.entity.AuthenticationResponse;
import dev.pioruocco.model.entity.User;
import dev.pioruocco.model.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//this class handles the logic of registering and authentication
@Service
public class AuthenticationService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    //regiistering a new user
    /*
    1. Creates a new User object from the request data.
    2. Hashes the user's password using the passwordEncoder.
    3. Saves the user details to the repository using userRepository.save().
    4. Generates a JWT token for the newly registered user using jwtService.generateToken().
    5. Returns an AuthenticationResponse object likely containing the generated token.
     */
    public AuthenticationResponse register(User request){
        User user = new User();

        //for devlopment purpose we use the setters
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    //authentication of a user
    /*
    1. Attempts to authenticate the user using the provided username and password.
       It does this by creating a "UsernamePasswordAuthenticationToken" Object and passing it
       to the "authenticationManager.authenticate()" method. This method interacts with configured
       authentication providers to verify user credentials.

    2. If authentication is successful (no exception thrown), retrieves the user details from
       the repository using "userRepository.findByUsername()".

    3. Generates a JWT token for the authenticated user using "jwtService.generateToken()".

    4. Returns an AuthenticationResponse object likely containing the generated token.
     */
    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }
}
