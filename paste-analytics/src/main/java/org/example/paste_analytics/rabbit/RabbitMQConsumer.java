package org.example.paste_analytics.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paste_analytics.PasteInfoRequest;
import org.example.paste_analytics.PasteInfoService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final PasteInfoService pasteInfoService;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(durable = "true", value = "${rabbitmq.queues.info-create.name"),
                    exchange = @Exchange("${rabbitmq.exchanges.paste-analytics}"),
                    key = "${rabbitmq.routing-keys.info-create.name}"
            )})
    public void receiveInfo(PasteInfoRequest pasteInfoRequest){
        log.info("Received request for creation {}", pasteInfoRequest);
        pasteInfoService.savePasteInfo(pasteInfoRequest);
    }
}
