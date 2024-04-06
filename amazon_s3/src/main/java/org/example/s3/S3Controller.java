package org.example.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @PostMapping("/upload/{key}")
    public ResponseEntity<String> uploadFile(@PathVariable("key") String key,
                                             @RequestBody String data){
        s3Service.putObject(bucketName, key, data.getBytes());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<String> deleteFile(@PathVariable("key") String key){
        s3Service.deleteObject(bucketName, key);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFiles(@RequestBody List<String> keys){
        s3Service.deleteObjects(bucketName, keys);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/load")
    public ResponseEntity<List<String>> loadAllFiles(){
        return ResponseEntity.ok(s3Service.getObjectKeys(bucketName));
    }

    @GetMapping("/load/{key}")
    public ResponseEntity<String> loadFile(@PathVariable("key") String key){
        log.info("Requested file with key {}", key);
        return ResponseEntity.ok(new String(s3Service.getObject(bucketName, key)));
    }

}
