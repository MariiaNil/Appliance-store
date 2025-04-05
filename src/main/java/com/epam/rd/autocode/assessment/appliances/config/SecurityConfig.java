package com.epam.rd.autocode.assessment.appliances.config;

import com.epam.rd.autocode.assessment.appliances.service.CustomUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                /*.csrf(csrf -> csrf
                        // Если h2-console или страницы восстановления пароля используют iframe или подобное,
                        // можно добавить исключения для CSRF
                        .ignoringRequestMatchers("/password/**")
                )*/
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                new CorsConfiguration().applyPermitDefaultValues()))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                /*.exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))*/
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/login", "/register", "/", "/change-lang", "/oauth2/**").permitAll()
                        .requestMatchers("/password/forgot-password", "/password/reset-password/**", "/password/reset-password-success", "/password/reset-password-error").permitAll()
                        .requestMatchers("/employees/**", "/employees/add-employee", "/employees/{id}/delete",
                                "/clients/**", "/orders/**", "appliances/add", "appliances/add-appliance").hasRole("EMPLOYEE")
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/appliances").hasAnyRole( "CLIENT", "EMPLOYEE")
                        .anyRequest().authenticated())
                /*.userDetailsService(customUserService)*/
                .formLogin(login -> login
                        .loginPage("/login")
                        /*.passwordParameter("password")
                        .usernameParameter("email")*/
                        .permitAll() // Разрешаем доступ к самой странице логина
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // После выхода перенаправляет на страницу логина
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}


// Создать страницу регистрации +
    // контроллер для нее +
    // роли для регистрации (не делаем) +
    // прикрутить сохранения пользователей в бд +
    // пароли
    // зброс паролей

/*

private final CustomUserService customUserService;
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/error", "/login", "/register", "/", "/change-lang").permitAll()
                    .requestMatchers("/employees/**").hasRole("EMPLOYEE")
                    .requestMatchers("/appliances").hasAnyRole( "CLIENT", "EMPLOYEE")

                    .anyRequest().authenticated())
            .userDetailsService(customUserService)
            */
/*.formLogin(withDefaults())
            .logout(withDefaults())*//*

            .formLogin(login -> login
                    .loginPage("/login")  // Указываем свою страницу логина
                    .permitAll() // Разрешаем доступ к самой странице логина
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login") // После выхода перенаправляет на страницу логина
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            );
    */
/*.sessionManagement(session -> session.maximumSessions(1));*//*

    return http.build();
}*/
