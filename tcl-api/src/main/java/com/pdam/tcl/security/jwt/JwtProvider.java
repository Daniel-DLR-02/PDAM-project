package com.pdam.tcl.security.jwt;

import com.pdam.tcl.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


@Log
@Service
public class JwtProvider {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret:SG0pv5jeZbYfrRMektCCvNxgvpKpAUIN}")
    private String jwtSecret;

    @Value("${jwt.duration:86400}")
    private int jwtLifeInSeconds;

    private JwtParser parser;

    @PostConstruct
    public void init(){
        parser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build();
        //Jwt parser initialize.
    }

    public String generateToken(Authentication authentication){

        // Actual user authenticating
        User user = (User) authentication.getPrincipal();

        // Instanciate the token expiration date
        Date tokenExpiration = Date.
                from(LocalDateTime
                        .now()
                        .plusSeconds(jwtLifeInSeconds)
                        .atZone(ZoneId.systemDefault()).toInstant());

        // Jwt builder
        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(user.getUuid().toString())
                .setIssuedAt(tokenExpiration)
                .claim("nickName", user.getNickname())
                .claim("role", user.getRole().name())
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public UUID getUserIdFromJwt(String token) {
        return UUID.fromString(parser.parseClaimsJwt(token).getBody().getSubject());
        // Gets user by the token
    }

    public boolean validateToken(String token ){

        try {
            // Doing the jtw parser
            parser.parseClaimsJwt(token);
            return true;
        }catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.info("Token error: " + ex.getMessage());
        }
        return false;
    }


}
