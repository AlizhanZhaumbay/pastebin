package org.example.s3;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Object is not exist.")
public class NoSuchObjectException extends RuntimeException{
}
