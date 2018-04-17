package org.faceview.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.faceview.user.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import static org.faceview.common.JwtConstants.AUTHORIZATION_HEADER;
import static org.faceview.common.JwtConstants.AUTHORIZATION_PREFIX;
import static org.faceview.common.JwtConstants.SECRET_ID;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final int EXPIRATION_MILLISECONDS = 12000000;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            return this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>()
                    )
            );

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
         String token = Jwts.builder().setSubject((((User) authResult.getPrincipal()).getUsername()))
                 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLISECONDS))
                 .signWith(SignatureAlgorithm.HS256, SECRET_ID.getBytes())
                 .compact();

         response.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + token);
         response.addHeader("Content-type", "application/json");
         response.getWriter().append("{\"Authorization\": \"" + AUTHORIZATION_PREFIX + token + "\"" +
                 ", \"id\": \"" + ((User) authResult.getPrincipal()).getId() +"\" " +
                 ", \"expires\": \"" + EXPIRATION_MILLISECONDS + "\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.addHeader("Content-type", "application/json");
        response.getWriter().append("{\"error\": {\"message\": \"Invalid Credentials\" } }");
    }
}
