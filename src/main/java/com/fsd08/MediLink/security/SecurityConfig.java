package com.fsd08.MediLink.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final UserDetailsServiceImpl userDetailsServiceImpl;
        private final JwtAuthenticationFilter authenticationFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity.csrf(AbstractHttpConfigurer::disable)
                                .cors(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(request -> request
                                                .requestMatchers("/", "/payment/**", "/videochat/**", "/aichat/**")
                                                .permitAll()
                                                .requestMatchers("/", "/register", "/login", "/verifyEmail/**","/getAllDoctors","/getAllUsers","/getUserById/**",
                                                                "/password-reset-request/**", "/reset-password/**","/testValidateToken","/getUserById/updatePatch/**")
                                                .permitAll()
                                                .requestMatchers("/", "/api/departments", "/api/departments/**")
                                                .permitAll()
                                                .requestMatchers("/", "/appointments/**").permitAll()
                                                .requestMatchers("/", "/api/messages", "/api/messages/**").permitAll()
                                                .requestMatchers("/", "/api/certificates/**", "/api/certificates/user**").permitAll()
                                                .requestMatchers("/", "/users", "/users/**").hasAuthority("DOCTOR"))
                                // .formLogin(withDefaults())
                                .sessionManagement(manager -> manager
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
                // .oauth2Login(withDefaults());

                return httpSecurity.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                var authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(userDetailsServiceImpl);
                authProvider.setPasswordEncoder(passwordEncoder());
                return authProvider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }
        // @Bean
        // public RequestCache refererRequestCache() { // (4)
        // return new HttpSessionRequestCache() {
        // @Override
        // public void saveRequest(HttpServletRequest request, HttpServletResponse
        // response) {
        // String referrer = request.getHeader("referer"); // (5)
        // if (referrer == null) {
        // referrer = request.getRequestURL().toString();
        // }
        // request.getSession().setAttribute("SPRING_SECURITY_SAVED_REQUEST",
        // new SimpleSavedRequest(referrer));
        //
        // }
        // };
        // }
}
