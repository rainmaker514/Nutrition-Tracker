package com.nutritiontracker.NutritionTrackerUserService.filter;

import com.nutritiontracker.NutritionTrackerUserService.constant.*;
import com.nutritiontracker.NutritionTrackerUserService.utility.JWTTokenProvider;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.nutritiontracker.NutritionTrackerUserService.constant.SecurityConstant.OPTION_HTTP_METHOD;
import static com.nutritiontracker.NutritionTrackerUserService.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
//authorize or not authorize any request only once
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private JWTTokenProvider jwtTokenProvider;

    public JWTAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {

        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase(OPTION_HTTP_METHOD)){
            response.setStatus(HttpStatus.OK.value());
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)){
                filterChain.doFilter(request, response);
                return;
            }
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            String email = jwtTokenProvider.getSubject(token);
            if(jwtTokenProvider.isTokenValid(email, token) && SecurityContextHolder.getContext().getAuthentication() == null){
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(email, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);
        }
    }


}