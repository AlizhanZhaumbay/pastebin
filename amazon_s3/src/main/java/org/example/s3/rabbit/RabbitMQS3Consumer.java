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

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQS3Consumer {
    @Value("${aws.bucket-name}")
    private String bucketName;

    private final S3Service s3Service;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(durable = "true", value = "${rabbitmq.queues.s3-create}"),
                    exchange = @Exchange("${rabbitmq.exchanges.s3.name}"),
                    key = "${rabbitmq.routing-keys.create}"
    )})
    public void receiveFile(PasteRequest pasteRequest){
        log.info("Received payload");
        s3Service.putObject(bucketName, pasteRequest.getKey(), pasteRequest.getData().getBytes());
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(durable = "true", value = "${rabbitmq.queues.s3-delete}"),
                    exchange = @Exchange("${rabbitmq.exchanges.s3.name}"),
                    key = "${rabbitmq.routing-keys.delete}"
            )})
    public void receiveFileDeletion(List<String> keys){
        log.info("Received deletion payload");
        s3Service.deleteObjects(bucketName, keys);
    }
}
