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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
                .antMatchers("/api/v1/comments/get/*", "/api/v1/comments/getAll", "/api/v1/comments/getCountByRecipe/*").permitAll()
                .antMatchers("/api/v1/users/get/*", "/api/v1/users/getAll", "/api/v1/users/checkFavorite/*", "/api/v1/users/get5Random").permitAll()
                .antMatchers("/api/v1/ingredients/get/*", "/api/v1/ingredients/getAll").permitAll()
                .antMatchers("/api/v1/recipe-ingredients/get/*", "/api/v1/recipe-ingredients/getAll", "/api/v1/recipe-ingredients/getByRecipe/*").permitAll()
                .antMatchers("/api/v1/reviews/get/*", "/api/v1/reviews/getAll", "/api/v1/reviews/getCountByRecipe/*", "/api/v1/reviews/getAvgRating/*", "/api/v1/reviews/containsReview/*", "/api/v1/reviews/getByRecipe/*", "/api/v1/reviews/getLikes/*", "/api/v1/reviews/isLikedByUser/*", "/api/v1/reviews/getAllByRecipe/*").permitAll()
                .antMatchers("/api/v1/recipes/get/*", "/api/v1/recipes/getAll").permitAll()
                .antMatchers("/api/v1/follows/get/*", "/api/v1/follows/getAll", "/api/v1/follows/following/*", "/api/v1/follows/followers/*", "/api/v1/follows/follows/*").permitAll()
                .antMatchers("/img/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}