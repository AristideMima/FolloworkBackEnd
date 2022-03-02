package com.followorkback.followorkback.security;

import com.followorkback.followorkback.filter.CustomAuthenticationFilter;
import com.followorkback.followorkback.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static java.util.Arrays.*;
import static org.springframework.http.HttpMethod.*;


@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.cors().and();

        // user routes
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers( "/api/login/**", "/token/refresh?**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers( "/api/usersByRole/{userRole}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(POST, "/api/user/edit/**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/user/delete/**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/users/**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");


        // Etudes routes
        http.authorizeRequests().antMatchers( "/api/etude/save/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers( "/api/etude/update/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers( "/api/etude/analysis/update/{username}/{action}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers( "/api/etude/manager/update/{username}/{action}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/etude/monitor/{genericCode}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/etude/etudes**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(DELETE, "/api/etude/delete/{genericCode}/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/etude/etudes/analyst/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/etude/etudes/manager/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/etude/stats/all**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/etude/stats/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");

        // Credit routes
        http.authorizeRequests().antMatchers( "/api/credit/save/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers( "/api/credit/update/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers( "/api/credit/analysis/update/{username}/{action}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers( "/api/credit/manager/update/{username}/{action}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/credit/monitor/{genericCode}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/credit/etudes**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(DELETE, "/api/credit/delete/{genericCode}/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/credit/credits/analyst/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/credit/credits/manager/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/credit/stats/{username}**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");
        http.authorizeRequests().antMatchers(GET, "/api/credit/stats/all**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");

        // Global
        http.authorizeRequests().antMatchers(GET, "/api/etude/name**").hasAnyAuthority( "ROLE_MANAGER","ROLE_ADMIN", "ROLE_ANALYST");

        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
        configuration.addAllowedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }


}
