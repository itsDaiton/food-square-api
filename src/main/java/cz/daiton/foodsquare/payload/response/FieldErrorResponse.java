package cz.daiton.foodsquare.payload.response;

import java.util.List;

public class FieldErrorResponse {

    private List<CustomFieldError> errorList;

    public FieldErrorResponse() {

    }

    public List<CustomFieldError> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<CustomFieldError> errorList) {
        this.errorList = errorList;
    }
}