package com.odiaz.moneymanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odiaz.moneymanager.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if( authHeader == null || !authHeader.startsWith("Bearer ") ){
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);

        try{
            String email = jwtUtils.extractEmail(token);

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                var userDetails = userDetailsService.loadUserByUsername(email);

                if(jwtUtils.validateToken(token, userDetails.getUsername())){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);

        }catch(JwtException e){
            System.out.println(e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            ObjectMapper mapper = new ObjectMapper();
            String msg = mapper.writeValueAsString(Map.of("message", "Token inválido"));
            response.getWriter().write(msg);

        }catch (UsernameNotFoundException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(e.getMessage());
        }

    }

}
