package org.example.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3Service {
    private final S3Client s3Client;

    public void putObject(String bucketName, String key, byte[] file) {
        String destinationKey = getDestinationKey(key);
        if (containsObject(bucketName, destinationKey)) {
            throw new ObjectAlreadyExistsException();
        }

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(destinationKey)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file));
    }

    public byte[] getObject(String bucketName, String key) {
        String destinationKey = getDestinationKey(key);
        if (!containsObject(bucketName, destinationKey)) {
            throw new NoSuchObjectException();
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(destinationKey)
                .build();

        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(getObjectRequest);
        try {
            return object.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getObjectKeys(String bucketName) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();

        ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjectsRequest);

        return listObjectsResponse.contents()
                .stream()
                .map(S3Object::key)
                .map(this::removePrefix)
                .collect(Collectors.toList());
    }

    public boolean containsObject(String bucketName, String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);

            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    public void deleteObject(String bucketName, String key) {
        String destinationKey = getDestinationKey(key);
        if (!containsObject(bucketName, destinationKey)) {
            throw new NoSuchObjectException();
        }
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(destinationKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public void deleteObjects(String bucketName, List<String> keys) {
        log.info("Starting to delete objects...");
        List<ObjectIdentifier> objectIdentifiers = keys
                .stream()
                .map(this::getObjectIdentifier)
                .toList();

        log.info("Object identifiers are set...");


        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder()
                        .objects(objectIdentifiers)
                        .build())
                .build();

        s3Client.deleteObjects(deleteObjectsRequest);

        log.info("Expired objects are deleted...");
    }

    private ObjectIdentifier getObjectIdentifier(String k) {
        return ObjectIdentifier.builder()
                .key(getDestinationKey(k)).build();
    }

    private String getDestinationKey(String key) {
        return "files/{key}".replace("{key}", key);
    }

    private String removePrefix(String key) {
        return key.replace("files/", "");
    }

}
