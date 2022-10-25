package cz.daiton.foodsquare.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InsertResponse {

    private Long id;

    private String message;
}
