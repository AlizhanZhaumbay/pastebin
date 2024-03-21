package org.example.api;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Paste {


    String shortLink;

    long expirationLengthInMinutes;

    LocalDateTime createdAt;
}
