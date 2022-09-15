package cz.daiton.foodsquare.security;

public class IncorrectUserException extends Exception {

    public IncorrectUserException(String message) {
        super(message);
    }

}
