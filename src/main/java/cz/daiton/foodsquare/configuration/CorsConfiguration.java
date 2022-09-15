package cz.daiton.foodsquare.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
                registry.addMapping("/api/v1/auth/login").allowedOrigins("http://localhost:3000").allowCredentials(true);
                registry.addMapping("/api/v1/meals/add").allowedOrigins("http://localhost:3000").allowCredentials(true);
                registry.addMapping("/api/v1/reviews/add").allowedOrigins("http://localhost:3000").allowCredentials(true);
                registry.addMapping("/api/v1/threads/add").allowedOrigins("http://localhost:3000").allowCredentials(true);
                registry.addMapping("/api/v1/posts/add").allowedOrigins("http://localhost:3000").allowCredentials(true);
            }
        };
    }

}
