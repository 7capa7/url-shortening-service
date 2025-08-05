package pl.patryk.shortener.exception;

public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException() {
        super("Tag already exists");
    }
}