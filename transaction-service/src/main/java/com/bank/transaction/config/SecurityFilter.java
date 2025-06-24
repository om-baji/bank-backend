package com.bank.transaction.config;

import com.bank.transaction.helpers.Helper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtValidator validator;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Helper helper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
            username = validator.extractUsername(token);
        }

        if(username != null && validator.validateTokenAndUsername(username,token) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            List<String> roles = validator.extractRoles(token);
            String user_id = validator.extractId(token);

            if(user_id == null) return;

            List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            helper.setCurrentId(user_id);
            helper.setCurrentUsername(username);
            helper.setCurrentRoles(roles);
        }

        filterChain.doFilter(request,response);
    }
}
