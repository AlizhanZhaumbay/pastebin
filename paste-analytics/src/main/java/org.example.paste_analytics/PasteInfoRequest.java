package org.example.paste_analytics;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PasteInfoRequest(
        @JsonProperty("shortLink")
        String pasteShortLink,
        String category,
        String pasteSize) {}

