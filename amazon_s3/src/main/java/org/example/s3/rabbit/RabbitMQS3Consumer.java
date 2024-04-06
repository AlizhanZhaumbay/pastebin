package org.example.s3.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.s3.PasteRequest;
import org.example.s3.S3Service;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQS3Consumer {
    @Value("${aws.bucket-name}")
    private String bucketName;

    private final S3Service s3Service;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(durable = "true", value = "${rabbitmq.queue.name}"),
                    exchange = @Exchange("${rabbitmq.exchange.name}"),
                    key = "${rabbitmq.routing-key.name}"
    )})
    public void receiveFile(PasteRequest pasteRequest){
        log.info("Received payload");
        s3Service.putObject(bucketName, pasteRequest.getKey(), pasteRequest.getData().getBytes());
    }
}
