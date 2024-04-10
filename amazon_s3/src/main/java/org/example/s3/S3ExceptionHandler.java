package org.example.s3;

import org.example.validator.InvalidRequestBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
public class S3ExceptionHandler {

    @ExceptionHandler(NoSuchObjectException.class)
    public ResponseEntity<String> noSuchObjectException(NoSuchObjectException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(String.format("""
                        The requested object does not exist with the specified key {%s}.
                        Please ensure that the object key is correct and exists.
                        """, exception.getKey()));
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<String> objectAlreadyExists(ObjectAlreadyExistsException exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(String.format("""
                        The object you are trying to create with key{%s} already exists.
                        Please choose a different key
                        """, exception.getKey()));
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<Set<String>> invalidPasteRequest(InvalidRequestBodyException exception){
        return ResponseEntity
                .badRequest()
                .body(exception.getErrorMessages());
    }
}
