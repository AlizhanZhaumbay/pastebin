package org.example.paste;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.amqp.RabbitMQPasteProducer;
import org.example.paste.client.PasteInfoClient;
import org.example.paste.client.S3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Log4j2
    public class PasteCleanupScheduler {

    private final S3Client s3Client;
    private final PasteRepository pasteRepository;
    private final RabbitMQPasteProducer pasteProducer;
    private final PasteInfoClient pasteInfoClient;

    @Value("${rabbitmq.exchanges.paste-s3.name}")
    private String s3Exchange;

    @Value("${rabbitmq.exchanges.paste-analytics.name}")
    private String pasteInfoExchange;

    @Value("${rabbitmq.routing-keys.paste-s3.deletion}")
    private String s3DeletionRoutingKey;

    @Value("${rabbitmq.routing-keys.paste-analytics.deletion}")
    private String pasteInfoDeletionRoutingKey;

    @Scheduled(fixedRate = 24, timeUnit = TimeUnit.HOURS)
    public void deleteExpiredObjects(){
        List<String> shortLinksS3 = s3Client.loadPasteShortLinks();
        List<String> shortLinksPasteInfo = pasteInfoClient.loadAllInfoShortLinks();

        List<String> shortLinksToRemoveS3 = shortLinksS3
                .stream()
                .filter(this::doesNotExistByShortLink)
                .toList();

        log.info("Collected the keys of s3 files... {}", shortLinksToRemoveS3);
        pasteProducer.send(s3Exchange, s3DeletionRoutingKey, shortLinksS3);


        List<String> shortLinksToRemovePasteInfo = shortLinksPasteInfo
                .stream()
                .filter(this::doesNotExistByShortLink)
                .toList();
        log.info("Collected the keys of paste-info... {}", shortLinksToRemoveS3);

        pasteProducer.send(pasteInfoExchange, pasteInfoDeletionRoutingKey, shortLinksToRemovePasteInfo);
    }

    private boolean doesNotExistByShortLink(String shortLink){
        return !pasteRepository.existsByShortLink(shortLink);
    }
}
