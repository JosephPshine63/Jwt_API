package dev.pioruocco.model.service;

import dev.pioruocco.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    //secret key for coding and encoding (256 bit)
    private final String SECRET_KEY="0f0362543031e7f490dbe3961848749b06c2d61a56e80c2bc6d4863f09962b16";

    //extracting a username
    /*
    This method extracts the username claim from a JWT token.

    It uses the extractClaim method with a pre-defined function (Claims::getSubject) to retrieve the
    subject claim, which typically holds the username.
     */
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //cchecling the validity of an user
    /*
    This method checks the validity of a JWT token for a specific user.
    It performs two checks:
        - It compares the username extracted with "extractUsername()" from the token with the
          username of the provided user object.
        - It calls isTokenExpired to ensure the token hasn't passed its expiration time.
    The method returns true if both checks pass, indicating a valid token for the user.
     */
    public boolean isValid(String token, UserDetails user){
        String username =  extractUsername(token);

        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    //checking if the token is expired
    /*
    This method determines if a JWT token has expired.
    It retrieves the expiration claim using "extractClaim()" and the "Claims::getExpiration" function.
    It compares the retrieved expiration date with the current date and time.
    The method returns true if the expiration date is earlier than the current date, signifying an expired token.
     */
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //extracting the time when the token expires
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    //getting a specified claim
    /*
    This generic method extracts a specific claim from a JWT token.
    It takes two arguments:
        - The token string itself.
        - A function (resolver) that specifies which claim to extract. This function takes a Claims object
          as input and returns the desired claim value (of type T).
    The method first calls "extraxtAllClaims()" to get all claims from the token.
    It then applies the provided resolver function to the extracted claims to retrieve the specific claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extraxtAllClaims(token);
        return resolver.apply(claims);
    }

    //extracting all the claims
    /*
    This method parses a JWT token and retrieves all its claims as a Claims object.

    - It uses the JJWT library to perform the parsing and verification steps.
    - The secret key is retrieved using "getSigninKey()" for verification.
    - The method returns the payload section of the parsed token, which contains all the claims.
     */
    private Claims extraxtAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //generating a token
    /*
    This method generates a new JWT token for a given user.
    It uses the JJWT library builder to construct the token.
    The method sets various claims in the token:
        -> Subject claim: Set to the username of the user.
        -> Issued At claim: Set to the current time.
        -> Expiration claim: Set to the current time plus 24 hours (you can adjust this expiration time as needed).

    The secret key is retrieved using "getSigninKey()" for signing the token.
    Finally, the method builds and returns the compact JWT string.

    (used in the authentication service)
     */
    public String generateToken(User user){
        String token = Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSigninKey())
                .compact();

        return token;
    }

    //converting the key
    /*
    This method converts the configured secret key (stored in SECRET_KEY) into a usable signing key for JJWT.
        1. It first decodes the Base64 encoded secret key string.
        2. Then, it uses the JJWT Keys class to create a HMAC SHA-256 key from the decoded byte array.
    This key is returned for signing and verifying JWT tokens.
     */
    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
