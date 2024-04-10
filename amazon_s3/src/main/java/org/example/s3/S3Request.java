package org.example.s3;

import jakarta.validation.constraints.NotNull;

public record S3Request(@NotNull(message = "Data should not be empty.") String data) {
}
