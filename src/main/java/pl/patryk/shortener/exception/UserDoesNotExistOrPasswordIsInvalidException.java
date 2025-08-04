package pl.patryk.shortener.exception;

public class UserDoesNotExistOrPasswordIsInvalidException extends RuntimeException {
    public UserDoesNotExistOrPasswordIsInvalidException() {
        super("User does not exist, or password is invalid.");
    }
}