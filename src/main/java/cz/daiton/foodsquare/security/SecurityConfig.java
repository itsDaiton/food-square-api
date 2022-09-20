package cz.daiton.foodsquare.security;

import cz.daiton.foodsquare.authentication.CustomAuthenticationEntryPoint;
import cz.daiton.foodsquare.authentication.CustomAuthenticationFilter;
import cz.daiton.foodsquare.exceptions.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint unauthorizedHandler;

    public SecurityConfig(CustomAuthenticationEntryPoint unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter authenticationTokenFilter() {
        return new CustomAuthenticationFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/register", "/api/v1/auth/login").anonymous()
                .antMatchers("/api/v1/comments/get/*", "/api/v1/comments/getAll").permitAll()
                .antMatchers("/api/v1/users/get/*", "/api/v1/users/getAll").permitAll()
                .antMatchers("/api/v1/ingredients/get/*", "/api/v1/ingredients/getAll").permitAll()
                .antMatchers("/api/v1/meal-ingredients/get/*", "/api/v1/meal-ingredients/getAll").permitAll()
                .antMatchers("/api/v1/likes/get/*", "/api/v1/likes/getAll").permitAll()
                .antMatchers("/api/v1/meals/get/*", "/api/v1/meals/getAll").permitAll()
                .antMatchers("/api/v1/reviews/get/*", "/api/v1/reviews/getAll").permitAll()
                .antMatchers("/api/v1/threads/get/*", "/api/v1/threads/getAll").permitAll()
                .antMatchers("/api/v1/posts/get/*", "/api/v1/posts/getAll").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}