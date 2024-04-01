package org.example.paste;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Paste {
    @Id
    String id;

    String shortLink;

    @Indexed(expireAfterSeconds = 0, direction = IndexDirection.ASCENDING)
    Date expirationTime;

    PasteExpiration expiration;

    LocalDateTime createdAt;

    public Paste(String shortLink, Date expirationTime, LocalDateTime createdAt) {
        this.shortLink = shortLink;
        this.expirationTime = expirationTime;
        this.createdAt = createdAt;
    }
}
