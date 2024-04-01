package org.example.paste.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "s3Client", url = "localhost:8083/api/v1/s3")
public interface S3Client {

    @PostMapping("/upload/{key}")
    void uploadPaste(@PathVariable("key") String key, String data);

    @GetMapping("/load/{key}")
    String loadPaste(@PathVariable("key") String key);

    @GetMapping("/load")
    List<String> loadPasteShortLinks();

    @DeleteMapping("/delete/{key}")
    void deletePaste(@PathVariable("key") String key);

    @DeleteMapping("/delete")
    void deletePastes(List<String> keys);
}
