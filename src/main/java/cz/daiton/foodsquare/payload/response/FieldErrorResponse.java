package cz.daiton.foodsquare.payload.response;

import cz.daiton.foodsquare.exceptions.CustomFieldError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FieldErrorResponse {

    private List<CustomFieldError> errorList;
}
