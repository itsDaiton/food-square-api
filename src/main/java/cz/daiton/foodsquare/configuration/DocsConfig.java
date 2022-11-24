package cz.daiton.foodsquare.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocsConfig {

    @Bean
    public OpenAPI getApiInfo() {
        return new OpenAPI()
                .info(new Info().title("Food Square API")
                .description("Public documentation for Food Square API. Made with OpenAPI.")
                .version("v1.0.0")
                .license(new License()
                        .name("MIT License")
                        .url("https://github.com/itsDaiton/food-square-api/blob/docs/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub repository")
                        .url("https://github.com/itsDaiton/food-square-api"));
    }
}
