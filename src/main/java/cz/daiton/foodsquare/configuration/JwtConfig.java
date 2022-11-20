package cz.daiton.foodsquare.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("jwtProperties")
@EnableConfigurationProperties()
@ConfigurationProperties(prefix = "app.jwt")
@NoArgsConstructor
@Getter
@Setter
public class JwtConfig {

    private String secretKey;

    private String cookieName;

    private int expirationTime;

    private boolean secure;

    private String sameSite;
}
