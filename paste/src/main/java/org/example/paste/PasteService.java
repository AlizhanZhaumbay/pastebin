package org.example.paste;

import lombok.RequiredArgsConstructor;
import org.example.amqp.RabbitMQPasteProducer;
import org.example.paste.client.HashGeneratorClient;
import org.example.paste.client.PasteInfoClient;
import org.example.paste.client.S3Client;
import org.example.paste.paste_info.PasteInfoRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.example.paste.Paste.PasteBuilder;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasteService {

    private final HashGeneratorClient hashGeneratorClient;
    private final PasteRepository pasteRepository;
    private final S3Client s3Client;
    private final PasteInfoClient pasteInfoClient;
    private final RabbitMQPasteProducer pasteProducer;

    @Value("${rabbitmq.exchanges.paste-s3.name}")
    private String s3Exchange;

    @Value("${rabbitmq.exchanges.paste-analytics.name}")
    private String infoExchange;

    @Value("${rabbitmq.routing-keys.paste-s3}")
    private String s3RoutingKey;

    @Value("${rabbitmq.routing-keys.paste-analytics.creation}")
    private String infoCreationRoutingKey;

    @Value("${rabbitmq.routing-keys.paste-analytics.visiting}")
    private String infoVisitingRoutingKey;


    private static final Map<PasteExpiration, Duration> EXPIRATION_INTERVALS;

    public String savePaste(PasteRequest pasteRequest) {
        if (pasteRequest.getData() == null || pasteRequest.getData().isEmpty())
            throw new InvalidParameterException("No data");

        String shortLink = UUID.randomUUID().toString();
//        String shortLink = hashGeneratorClient.createHash();
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

        PasteDTO pasteDTO = new PasteDTO(shortLink, pasteRequest.getData());
        pasteProducer.send(
                s3Exchange,
                s3RoutingKey,
                pasteDTO);


        PasteInfoRequest pasteInfoRequest = new PasteInfoRequest(shortLink,
                pasteRequest.getCategory(),
                String.format("%f.2 KB", calculateSizeInKB(pasteRequest.getData())));

        pasteProducer.send(
                infoExchange,
                infoCreationRoutingKey,
                pasteInfoRequest
        );

//        pasteInfoClient.createInfo(pasteInfoRequest);
//        s3Client.uploadPaste(shortLink, pasteRequest.getData());

        pasteRepository.insert(pasteBuilder.build());

        return shortLink;
    }

    public String loadPaste(String shortLink) {
        Optional<Paste> optionalPaste = pasteRepository.findByShortLink(shortLink);

        if (optionalPaste.isEmpty()) throw new PasteNotFoundException();

        Paste paste = optionalPaste.get();
//        String pasteData = s3Client.loadPaste(shortLink);

        PasteExpiration pasteExpiration = paste.getExpiration();
//        if (pasteExpiration == null) return pasteData;
        pasteProducer.send(
                infoExchange,
                infoVisitingRoutingKey,
                shortLink
        );
        if (pasteExpiration.equals(PasteExpiration.BURN_AFTER_READ)) {
            pasteRepository.delete(paste);
            pasteInfoClient.deleteInfo(shortLink);
            s3Client.deletePaste(shortLink);
        }
        return "";
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

    private double calculateSizeInKB(String data) {
        int byteLength = data.getBytes().length;

        return (double) byteLength / 1024;
    }
}
