package cz.daiton.foodsquare.authentication.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    private String username;

    public String password;

}
