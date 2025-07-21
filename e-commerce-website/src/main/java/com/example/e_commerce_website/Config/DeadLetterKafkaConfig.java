package com.example.e_commerce_website.Config;

import com.example.e_commerce_website.Model.DeadLetterMessage;
import com.example.e_commerce_website.Serializer.XmlSerializer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DeadLetterKafkaConfig {

    @Bean
    public ProducerFactory<Long, DeadLetterMessage> deadLetterProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Long, DeadLetterMessage> deadLetterKafkaTemplate() {
        return new KafkaTemplate<>(deadLetterProducerFactory());
    }

	@Bean
	public ProducerFactory<Long, DeadLetterMessage> deadLetterXmlProducerFactory() {
	    Map<String, Object> configProps = new HashMap<>();
	    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    return new DefaultKafkaProducerFactory<>(configProps,
	            new LongSerializer(),
	            new XmlSerializer<>(DeadLetterMessage.class));
	}
	
	@Bean(name = "deadLetterXmlKafkaTemplate")
	public KafkaTemplate<Long, DeadLetterMessage> deadLetterXmlKafkaTemplate() {
	    return new KafkaTemplate<>(deadLetterXmlProducerFactory());
	}

}
