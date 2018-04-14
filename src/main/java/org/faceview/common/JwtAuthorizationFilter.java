package org.faceview.common;

import io.jsonwebtoken.Jwts;
import org.faceview.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.faceview.common.JwtConstants.AUTHORIZATION_HEADER;
import static org.faceview.common.JwtConstants.AUTHORIZATION_PREFIX;
import static org.faceview.common.JwtConstants.SECRET_ID;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(AUTHORIZATION_HEADER);

        if(header == null || !header.startsWith(AUTHORIZATION_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken  authenticationToken = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
            String token = request.getHeader(AUTHORIZATION_HEADER).replace(AUTHORIZATION_PREFIX, "");

            String user = Jwts.parser()
                    .setSigningKey(SECRET_ID.getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            if(user != null) {
                return new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        this.userService.loadUserByUsername(user).getAuthorities());
            }

        return null;
    }
}
