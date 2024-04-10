package org.example.paste;

public class InvalidPasteExpirationException extends RuntimeException {
    public InvalidPasteExpirationException(String message) {
        super(message);
    }
}
