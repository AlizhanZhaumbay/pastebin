package org.example.paste;

import lombok.RequiredArgsConstructor;
import org.example.paste.client.HashGeneratorClient;
import org.example.paste.client.S3Client;
import org.springframework.stereotype.Service;
import org.example.paste.Paste.PasteBuilder;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasteService {

    private final HashGeneratorClient hashGeneratorClient;
    private final PasteRepository pasteRepository;
    private final S3Client s3Client;

    private static final Map<PasteExpiration, Duration> EXPIRATION_INTERVALS;

    public String savePaste(PasteRequest pasteRequest) {
        if (pasteRequest.getData() == null || pasteRequest.getData().isEmpty())
            throw new InvalidParameterException("No data");

        String shortLink = hashGeneratorClient.createHash();
        PasteBuilder pasteBuilder = Paste.builder()
                .shortLink(shortLink)
                .createdAt(LocalDateTime.now());

        PasteExpiration pasteExpiration = pasteRequest.getPasteExpiration();
        if (pasteExpiration != null) {

            if (!pasteExpiration.equals(PasteExpiration.BURN_AFTER_READ)) {
                pasteBuilder.expirationTime(getExpirationDate(pasteExpiration));
            }

            pasteBuilder.expiration(pasteExpiration);
        }

        s3Client.uploadPaste(shortLink, pasteRequest.getData());
        pasteRepository.insert(pasteBuilder.build());

        return shortLink;
    }

    public String loadPaste(String shortLink) {
        Optional<Paste> optionalPaste = pasteRepository.findByShortLink(shortLink);

        if (optionalPaste.isEmpty()) throw new PasteNotFoundException();

        Paste paste = optionalPaste.get();
        String pasteData = s3Client.loadPaste(shortLink);

        PasteExpiration pasteExpiration = paste.getExpiration();
        if (pasteExpiration == null) return pasteData;

        if (pasteExpiration.equals(PasteExpiration.BURN_AFTER_READ)) {
            pasteRepository.delete(paste);
            s3Client.deletePaste(shortLink);
        }

        return pasteData;
    }

    private Date getExpirationDate(PasteExpiration pasteExpiration) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration interval = EXPIRATION_INTERVALS.get(pasteExpiration);
        return convertToDate(currentTime.plus(interval));
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    static {
        EXPIRATION_INTERVALS = Map.of(PasteExpiration.TEN_MINUTES, Duration.ofMinutes(10),
                PasteExpiration.ONE_HOUR, Duration.ofHours(1),
                PasteExpiration.ONE_DAY, Duration.ofDays(1));
    }

    public String pasteExists(String shortLink) {
        if(!pasteRepository.existsByShortLink(shortLink)) throw new PasteNotFoundException();

        return "EXISTS";
    }
}
