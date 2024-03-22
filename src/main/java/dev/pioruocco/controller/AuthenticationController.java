package dev.pioruocco.controller;

import dev.pioruocco.model.entity.AuthenticationResponse;
import dev.pioruocco.model.entity.User;
import dev.pioruocco.model.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    //registration post endpoint
    /*
    Accepts a POST request at the "/register" endpoint.
        1. Expects a User object in the request body.
        2. Delegates registration to the authenticationService.
        3. Returns a 200 OK response with an AuthenticationResponse object
           containing information like a generated JWT token.
     */
    @PostMapping(path = "/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    //login post endpoint
    /*
        Handles user login:

        1. Accepts a POST request at the "/login" endpoint.
        2. Expects a User object in the request body, likely containing username and password.
        3. Delegates authentication to the authenticationService.
        4. Returns a 200 OK response with an AuthenticationResponse object
           likely containing authentication details or a JWT token.
             */
    @PostMapping(path = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
