package cz.daiton.foodsquare.appuser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDto {

    private Long appUser;

    private Long content;

    private String type;

}
