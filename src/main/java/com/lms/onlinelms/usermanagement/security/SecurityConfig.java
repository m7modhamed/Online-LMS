package com.lms.onlinelms.usermanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
                .exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(new JwtAuthFilter(userAuthenticationProvider,exceptionResolver), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/login", "/register/**" ,"/verifyEmail/**","/resetPassword/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/verifyEmail/**" , "/forgot-password-request/**" , "/courses").permitAll()
                        .requestMatchers(HttpMethod.POST,"/courses" , "/courses/*/sections" , "/lessons/*/media" , "sections/*/lessons").hasRole("INSTRUCTOR")
                        .requestMatchers(HttpMethod.GET, "/categories","/courses/*/publishRequest" , "/instructor/*/courses","/instructor/*/courses/*" , "/courses/*/archive").hasRole("INSTRUCTOR")
                        .requestMatchers(HttpMethod.DELETE , "/courses/*").hasRole("INSTRUCTOR")
                        .requestMatchers(HttpMethod.POST,"/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "courses/*/publish" , "/review/courses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/students/*/courses/*" ).hasRole("STUDENT")
                        .requestMatchers(HttpMethod.POST , "/students/*/courses/*/enroll").hasRole("STUDENT")

                        .anyRequest().authenticated());

        return http.build();
    }


}
