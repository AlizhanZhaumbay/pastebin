package org.example.hash_gen;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/v1/hash")
@RequiredArgsConstructor
public class HashGeneratorController {

    private final HashGeneratorService hashGeneratorService;


    @PostMapping()
    public ResponseEntity<String> createHash() {
        return ResponseEntity.ok(hashGeneratorService.generateUniqueHash());
    }
}
