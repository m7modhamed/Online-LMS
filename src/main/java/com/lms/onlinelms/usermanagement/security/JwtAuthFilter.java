package com.lms.onlinelms.usermanagement.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lms.onlinelms.usermanagement.exception.MismatchTokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationProvider userAuthenticationProvider;

    private final HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String[] authElements = header.split(" ");
            if (authElements.length == 2
                    && "Bearer".equals(authElements[0])) {
                String token = authElements[1];

                DecodedJWT decodedJWT = JWT.decode(token);
                boolean isRefreshToken = decodedJWT.getClaim("tokenType").asString().equals("refresh_token");
                String requestURI = request.getRequestURI();

                if (isRefreshToken && !"/refresh_Token".equals(requestURI)) {
                    throw new MismatchTokenType();
                }
                SecurityContextHolder.getContext().setAuthentication(
                        userAuthenticationProvider.validateAccessTokenStrongly(token, request));

            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }

}
