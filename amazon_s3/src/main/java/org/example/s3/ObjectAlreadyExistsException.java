package org.example.s3;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Object already exists.")
public class ObjectAlreadyExistsException extends RuntimeException{
}
