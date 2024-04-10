package org.example.paste;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans({@ComponentScan("org.example.amqp"),
@ComponentScan("org.example.validator")})
public class PasteConfig {
}
