package pl.patryk.shortener.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException() {
        super("Please provide valid data.");
    }
}