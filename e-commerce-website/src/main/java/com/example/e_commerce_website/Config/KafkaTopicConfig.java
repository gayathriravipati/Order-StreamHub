package com.example.e_commerce_website.Config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
	@Bean
    public NewTopic orderCreatedUS() {
        return new NewTopic("order-created-US", 2, (short) 1);
    }

    @Bean
    public NewTopic orderCreatedUK() {
        return new NewTopic("order-created-UK", 2, (short) 1);
    }
    
    @Bean
    public NewTopic orderDeadLetterTopic() {
        return new NewTopic("order-dead-letter", 1, (short) 1);
    }
    
 // XML Topics
    @Bean
    public NewTopic orderCreatedUSXml() {
        return new NewTopic("order-created-US-xml", 2, (short) 1);
    }

    @Bean
    public NewTopic orderCreatedUKXml() {
        return new NewTopic("order-created-UK-xml", 2, (short) 1);
    }

    @Bean
    public NewTopic orderDeadLetterXml() {
        return new NewTopic("order-dead-letter-xml", 1, (short) 1);
    }

}
