package dev.pioruocco.model.entity;

//class that represents the response of the AuthenticationService
/*
AuthenticationResponse class serves as a simple container for holding and providing
the JWT token generated during user registration or authentication.
 */
public class AuthenticationResponse {

    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
