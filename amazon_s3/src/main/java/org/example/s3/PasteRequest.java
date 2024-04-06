package org.example.s3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PasteRequest {
    @JsonProperty("shortLink")
    private String key;
    @JsonProperty("data")
    private String data;
}
