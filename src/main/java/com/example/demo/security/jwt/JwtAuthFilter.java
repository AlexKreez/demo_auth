package com.example.demo.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {//OncePerRequestFilter, чтобы обрабатывать каждый запрос только один раз
//  Этот фильтр проверяет, есть ли JWT-токен в заголовке Authorization
//  Извлекает логин пользователя из токена
//  Проверяет, действителен ли токен
//  Авторизует пользователя в системе, если токен корректен
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("🔍 Входящий запрос: {}", request.getServletPath());

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOGGER.warn("⚠️ Неправильный или пустой заголовок Authorization");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); //Удаляем Bearer (7 символов) и получаем чистый токен
        final String userLogin = jwtService.extractUsername(jwt);


        if (userLogin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//              Если пользователь не аутентифицирован, загружаем его через UserDetailsService
//              Проверяем, валиден ли токен (jwtService.isTokenValid())
//              Если да, создаем объект авторизации UsernamePasswordAuthenticationToken и сохраняем в SecurityContext
            UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                LOGGER.info("✅ Пользователь '{}' аутентифицирован с ролями: {}", userDetails.getUsername(), userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                LOGGER.error("❌ Неправильный JWT токен");
            }
        } else {
            LOGGER.warn("❌ Пользователь не найден или уже аутентифицирован");
        }

        filterChain.doFilter(request, response);
    }
}
