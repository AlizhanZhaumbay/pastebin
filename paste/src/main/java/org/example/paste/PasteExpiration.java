package org.example.paste;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor

public enum PasteExpiration {
    BURN_AFTER_READ("bar"),
    TEN_MINUTES("10-min"),
    ONE_HOUR("1-hour"),
    ONE_DAY("1-day");

    final String expiration;


    @JsonValue
    public String getExpiration() {
        return expiration;
    }

    @JsonCreator
    public static PasteExpiration fromString(String value) {
        for (PasteExpiration pasteExpiration : PasteExpiration.values()) {
            if (pasteExpiration.expiration.equals(value)) {
                return pasteExpiration;
            }
        }
        throw new InvalidPasteExpirationException("Unknown expiration value: " + value);
    }
}
