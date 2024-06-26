package org.example.paste;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = RedisRepositoriesAutoConfiguration.class)
@EnableFeignClients
@EnableScheduling
@RequiredArgsConstructor
public class PasteApplication {
    public static void main(String[] args) {
        SpringApplication.run(PasteApplication.class, args);
    }
}
