package org.example.paste_analytics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class PasteInfoNotFoundException extends RuntimeException {
    private final String shortLink;
}
