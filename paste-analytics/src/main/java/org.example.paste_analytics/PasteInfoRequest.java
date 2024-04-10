package org.example.paste_analytics;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public record PasteInfoRequest(
        @JsonProperty("shortLink")
        @NotEmpty(message = "ShortLink should not be empty.")
        String pasteShortLink,

        @NotEmpty(message = "Category should not be empty.")
        String category,

        @NotEmpty(message = "Paste size should be defined.")
        String pasteSize) {}

