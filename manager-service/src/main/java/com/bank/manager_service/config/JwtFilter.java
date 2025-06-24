package com.bank.manager_service.config;

import com.bank.manager_service.helpers.Helper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private Helper helper;

    @Autowired
    private JwtValidator validator;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        String username = null;
        String token = null;

        if(header != null && header.startsWith("Bearer")) {
            token = header.substring(7);
            username = validator.extractUsername(token);
        }

        if(username != null && validator.validateTokenAndUsername(token,username)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            List<String> roles = validator.extractRoles(token);
            String user_id = validator.extractId(token);

            List<GrantedAuthority> authorities = roles.stream().map(role ->
                    new SimpleGrantedAuthority(role)).collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            helper.setCurrentUsername(username);
            helper.setUserId(user_id);
            helper.setRoles(roles);
        }

        filterChain.doFilter(request,response);
    }
}
