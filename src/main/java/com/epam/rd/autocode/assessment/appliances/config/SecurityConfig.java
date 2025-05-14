package com.epam.rd.autocode.assessment.appliances.config;

import com.epam.rd.autocode.assessment.appliances.aspect.Loggable;
import com.epam.rd.autocode.assessment.appliances.service.CustomUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserService customUserService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Loggable
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                /*.csrf(AbstractHttpConfigurer::disable)*/
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/password/**")
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                new CorsConfiguration().applyPermitDefaultValues()))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                /*.exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))*/
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/errors/403")
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendRedirect("/errors/401")))
                        /*.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))*/
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/errors/**", "/error", "/login", "/register", "/", "/change-lang", "/oauth2/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/password/forgot-password", "/password/reset-password/**", "/password/reset-password-success", "/password/reset-password-error").permitAll()
                        .requestMatchers("/employees/**", "/employees/add-employee", "/employees/{id}/delete",
                                "/clients/**",  "appliances/add", "appliances/add-appliance", "categories/add", "categories/add-category").hasRole("EMPLOYEE")
                        .requestMatchers("/h2-console/**", "/styles/**", "/images/**", "/js/**", "/bootstrap/**", "/categories**", "/uploads/**").permitAll()
                        .requestMatchers("/orders/my-orders").permitAll()
                        .requestMatchers("/appliances").hasAnyRole( "CLIENT", "EMPLOYEE")
                        .requestMatchers("/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                /*.userDetailsService(customUserService)*/
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/errors/login-error")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider);

        return http.build();
    }

}

