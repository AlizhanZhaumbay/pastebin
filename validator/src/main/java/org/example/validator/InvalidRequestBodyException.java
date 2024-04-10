package org.example.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class InvalidRequestBodyException extends RuntimeException {

    private final Set<String> errorMessages;
}
