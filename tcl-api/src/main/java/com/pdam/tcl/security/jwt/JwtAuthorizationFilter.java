package com.pdam.tcl.security.jwt;

import com.pdam.tcl.model.User;
import com.pdam.tcl.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserService userService;


    private String getRequestJwt(HttpServletRequest request){
        String token = request.getHeader(jwtProvider.TOKEN_HEADER);
        // Test token got of the request.Tests format.
        if(StringUtils.hasText(token) && token.startsWith(jwtProvider.TOKEN_PREFIX)){

            return token.substring(JwtProvider.TOKEN_PREFIX.length());
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getRequestJwt(request);

        try{
            if(StringUtils.hasText(token) && jwtProvider.validateToken(token)){

                Optional<User> userOpt = userService.findUserByUuid(jwtProvider.getUserIdFromJwt(token));

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    user.getRole(),
                                    user.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);


                }
            }

        } catch (Exception ex) {
            log.info("Can't stablish security context (" + ex.getMessage() + ")");
        }

        filterChain.doFilter(request, response);
    }
}
