package org.example.paste;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasteRequest {
    @NotEmpty(message = "Data should not be empty.")
    String data;

    @JsonProperty("expiration")
    PasteExpiration pasteExpiration;

    @NotEmpty(message = "Category should not be empty.")
    @Size(min = 1, max = 50, message = "Category size should be in range of (1 - 50)")
    String category;
}
