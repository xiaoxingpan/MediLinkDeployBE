package com.fsd08.MediLink.security;

import com.fsd08.MediLink.MediLinkApplication;
import com.fsd08.MediLink.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(MediLinkApplication.class);


    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,

                                    FilterChain filterChain) throws ServletException, IOException {
        logger.info("inside filter");
        String token =null;
        String username =null;
        String authHeader =request.getHeader("Authorization");
        if(authHeader!=null && authHeader.startsWith("Bearer")){
            token= authHeader.substring(7);
            username = jwtService.extractUsernameFromToken(token);
            if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails= userDetailsService.loadUserByUsername(username);
                if(jwtService.validateToken(token,userDetails)){
                    var authToken = new UsernamePasswordAuthenticationToken(token,null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
            filterChain.doFilter(request, response);

        }
        else{
            filterChain.doFilter(request, response);
            return;

        }
    }
}
