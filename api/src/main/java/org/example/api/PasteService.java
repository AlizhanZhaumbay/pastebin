package org.example.api;

import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PasteService {

    private final HashGeneratorClient hashGeneratorClient;

    //TODO remove after injecting db
    private final Map<String, String> dataStore;

    public PasteService(HashGeneratorClient hashGeneratorClient) {
        this.hashGeneratorClient = hashGeneratorClient;
        this.dataStore = new HashMap<>();
    }

    public String savePaste(PasteDto pasteRequest){
        if(pasteRequest.getData() == null || pasteRequest.getData().isEmpty())
            throw new InvalidParameterException("No data");

        String shortLink = hashGeneratorClient.createHash();

        Paste.PasteBuilder pasteBuilder = Paste
                .builder()
                .createdAt(LocalDateTime.now())
                .shortLink(shortLink);

        //Setting expiration if exists
        if(pasteRequest.getExpirationLengthInMinutes() != 0){
            pasteBuilder.expirationLengthInMinutes(pasteRequest.getExpirationLengthInMinutes());
        }

        //TODO Save to db;

        dataStore.put(shortLink, pasteRequest.getData());

        return shortLink;
    }

    public String getPasteByHash(String hash) {
        return dataStore.get(hash);
    }
}
