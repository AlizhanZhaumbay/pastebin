package org.example.hash_gen;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@Log4j2
@RequiredArgsConstructor
public class HashGeneratorService {
    private final HashRepository hashRepository;

    public synchronized String generateUniqueHash() {
        log.info("starting to generate a hash with thread {}...", Thread.currentThread().getName());

        UrlHash urlHash = hashRepository.save(new UrlHash());
        int length = 12;

        byte[] uniqueNumberBytes = String.valueOf(urlHash.getId()).getBytes(StandardCharsets.UTF_8);
        byte[] sha256Hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            sha256Hash = digest.digest(uniqueNumberBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create a hash");
        }


        String base64EncodedHash = Base64.getUrlEncoder().encodeToString(sha256Hash);


        String finalHash = base64EncodedHash.substring(0, length);
        urlHash.setHash(finalHash);

        hashRepository.flush();

        log.info("ending to generate a hash with id {} with thread {}...", urlHash.getId(), Thread.currentThread().getName());


        return finalHash;
    }
}
