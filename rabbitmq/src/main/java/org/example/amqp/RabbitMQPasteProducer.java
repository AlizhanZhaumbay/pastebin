package org.example.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQPasteProducer {
    private final RabbitTemplate rabbitTemplate;

    public void send(String exchange, String routingKey, Object payload){
        log.info("Sending to {} using routingKey {}. Payload: {}", exchange, routingKey, payload);
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
        log.info("Sent to {} using routingKey {}. Payload: {}", exchange, routingKey, payload);
    }
}
