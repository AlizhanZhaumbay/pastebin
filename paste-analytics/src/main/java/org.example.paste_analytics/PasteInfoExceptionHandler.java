package org.example.paste_analytics;

import org.example.validator.InvalidRequestBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@RestControllerAdvice
public class PasteInfoExceptionHandler {

    @ExceptionHandler(PasteInfoNotFoundException.class)
    public ResponseEntity<String> pasteInfoNotFoundException(PasteInfoNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(String.format("""
                       Paste info not found with shortLink {%s}
                        """, exception.getShortLink()));
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    public ResponseEntity<Set<String>> invalidPasteRequest(InvalidRequestBodyException exception){
        return ResponseEntity
                .badRequest()
                .body(exception.getErrorMessages());
    }
}
