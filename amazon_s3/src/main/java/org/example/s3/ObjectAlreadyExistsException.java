package org.example.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ObjectAlreadyExistsException extends RuntimeException{

    private final String key;
}
