package cz.daiton.foodsquare.security;

import cz.daiton.foodsquare.authentication.CustomAuthenticationEntryPoint;
import cz.daiton.foodsquare.authentication.CustomAuthenticationFilter;
import cz.daiton.foodsquare.exceptions.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;


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
        return new BCryptPasswordEncoder(10, new SecureRandom());
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
                .antMatchers("/api/v1/auth/register").anonymous()
                .antMatchers("/api/v1/auth/login").permitAll()
                .antMatchers("/api/v1/comments/get/*", "/api/v1/comments/getAll", "/api/v1/comments/getCountByRecipe/*", "/api/v1/comments/getAllByRecipe/*", "/api/v1/comments/getLikes/*", "/api/v1/comments/getAllByUser/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/users/**", "/api/v1/users").permitAll()
                .antMatchers("/api/v1/ingredients/get/*", "/api/v1/ingredients/getAll").permitAll()
                .antMatchers("/api/v1/recipe-ingredients/get/*", "/api/v1/recipe-ingredients/getAll", "/api/v1/recipe-ingredients/getByRecipe/*", "/api/v1/recipe-ingredients/calculateNutritionAnalysis/*").permitAll()
                .antMatchers("/api/v1/reviews/get/*", "/api/v1/reviews/getAll", "/api/v1/reviews/getCountByRecipe/*", "/api/v1/reviews/getAvgRating/*", "/api/v1/reviews/containsReview/*", "/api/v1/reviews/getByRecipe/*", "/api/v1/reviews/getLikes/*", "/api/v1/reviews/getAllByRecipe/*", "/api/v1/reviews/getAllByUser/*").permitAll()
                .antMatchers("/api/v1/recipes/get/*", "/api/v1/recipes/getAll", "/api/v1/recipes/getAllExtended", "/api/v1/recipes/getAllByUser/*").permitAll()
                .antMatchers("/api/v1/follows/get/*", "/api/v1/follows/getAll", "/api/v1/follows/following/*", "/api/v1/follows/followers/*", "/api/v1/follows/follows/*").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/api/v1/meal-planning/*").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}