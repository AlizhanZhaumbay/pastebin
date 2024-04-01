package org.example.paste.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "hashGenerator", url = "localhost:8082/api/v1/hash")
public interface HashGeneratorClient {

    @PostMapping
    String createHash();
}
