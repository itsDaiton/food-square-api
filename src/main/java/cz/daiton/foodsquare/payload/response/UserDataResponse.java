package cz.daiton.foodsquare.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserDataResponse {

    private Long id;

    private String username;

    private String email;

    private List<String> roles;
}
