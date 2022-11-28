package cz.daiton.foodsquare.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("dateProperties")
@EnableConfigurationProperties()
@ConfigurationProperties(prefix = "server.timezone")
@NoArgsConstructor
@Getter
@Setter
public class DateConfig {

    private long offset;

}
