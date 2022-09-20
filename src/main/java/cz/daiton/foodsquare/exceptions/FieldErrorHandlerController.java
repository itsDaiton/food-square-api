package cz.daiton.foodsquare.exceptions;

import cz.daiton.foodsquare.payload.response.FieldErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class FieldErrorHandlerController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();
        List<CustomFieldError> errorList = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error -> {
            CustomFieldError fieldError = new CustomFieldError();
            fieldError.setField(((FieldError) error).getField());
            fieldError.setMessage(error.getDefaultMessage());
            errorList.add(fieldError);
        }));

        fieldErrorResponse.setErrorList(errorList);

        return new ResponseEntity<>(fieldErrorResponse, status);
    }
}
