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

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {//OncePerRequestFilter, чтобы обрабатывать каждый запрос только один раз
//  Этот фильтр проверяет, есть ли JWT-токен в заголовке Authorization
//  Извлекает логин пользователя из токена
//  Проверяет, действителен ли токен
//  Авторизует пользователя в системе, если токен корректен
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("🔍 Входящий запрос: " + request.getServletPath());
        System.out.println("🔍 SecurityContext перед фильтром: " + SecurityContextHolder.getContext());

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ неправильный запрос (добавьте Bearer ) или запрос пустой");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); //Удаляем Bearer (7 символов) и получаем чистый токен
        final String userLogin = jwtService.extractUsername(jwt);

        System.out.println("🔑 Extracted username from JWT: " + userLogin);

        if (userLogin != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//              Если пользователь не аутентифицирован, загружаем его через UserDetailsService
//              Проверяем, валиден ли токен (jwtService.isTokenValid())
//              Если да, создаем объект авторизации UsernamePasswordAuthenticationToken и сохраняем в SecurityContext
            UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("✅ Пользователь " + userDetails.getUsername() + " аутентифицирован с ролями: " + userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("❌ Неправильный JWT");
            }
        } else {
            System.out.println("❌ Пользователь не найден или уже аутентифицирован");
        }

        filterChain.doFilter(request, response);
    }
}
