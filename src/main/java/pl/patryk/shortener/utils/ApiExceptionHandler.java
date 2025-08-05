package pl.patryk.shortener.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.patryk.shortener.exception.InvalidDataException;
import pl.patryk.shortener.exception.TagAlreadyExistsException;
import pl.patryk.shortener.exception.UserAlreadyExistsException;
import pl.patryk.shortener.exception.UserDoesNotExistOrPasswordIsInvalidException;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(UserDoesNotExistOrPasswordIsInvalidException.class)
    public ResponseEntity<BaseResponse> userDoesNotExistOrPassIsInvalid(UserDoesNotExistOrPasswordIsInvalidException ex) {
        BaseResponse exceptionResponse = new BaseResponse(401, ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<BaseResponse> invalidData(InvalidDataException exception) {
        BaseResponse exceptionResponse = new BaseResponse(400, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<BaseResponse> userAlreadyExists(UserAlreadyExistsException exception) {
        BaseResponse exceptionResponse = new BaseResponse(409, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TagAlreadyExistsException.class)
    public ResponseEntity<BaseResponse> tagAlreadyExists(TagAlreadyExistsException exception) {
        BaseResponse exceptionResponse = new BaseResponse(409, exception.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }
}
