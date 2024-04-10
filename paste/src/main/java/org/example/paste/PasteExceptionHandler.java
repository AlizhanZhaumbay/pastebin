package org.example.paste;

import org.example.validator.InvalidRequestBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
public class PasteExceptionHandler {


    @ExceptionHandler(InvalidPasteExpirationException.class)
    public ResponseEntity<String> invalidPasteExpiration(){
        return ResponseEntity
                .badRequest()
                .body("""
                        Error: Invalid input for paste expiration.
                        Please provide a valid expiration option.
                        Valid options include: 'never(default)', 'bar(burn after read)', '10-min', '1-hour', '1-day'.""");
    }

    @ExceptionHandler(PasteNotFoundException.class)
    public ResponseEntity<String> pasteNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("""
                Error: Paste not found.
                The requested paste could not be found.
                Please verify the paste hash and try again.
                """);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<Set<String>> invalidPasteRequest(InvalidRequestBodyException exception){
        return ResponseEntity
                .badRequest()
                .body(exception.getErrorMessages());
    }
}
