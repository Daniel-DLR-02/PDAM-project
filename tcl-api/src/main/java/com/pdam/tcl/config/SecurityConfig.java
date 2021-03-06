package com.pdam.tcl.config;


import com.pdam.tcl.security.jwt.JwtAccessDeniedHandler;
import com.pdam.tcl.security.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthorizationFilter filter;
    private final JwtAccessDeniedHandler accessDeniedHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/login").anonymous()
                .antMatchers(HttpMethod.POST, "/films/").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/films/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/films/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/hall/").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/hall/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/hall/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/session/").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/session/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/session/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/ticket/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/ticket/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/user/new-admin").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/user/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/user/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/user/admin").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/user").hasAuthority("ADMIN")
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api-docs").permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated();


        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        // Para dar acceso a h2
        http.headers().frameOptions().disable();


    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}