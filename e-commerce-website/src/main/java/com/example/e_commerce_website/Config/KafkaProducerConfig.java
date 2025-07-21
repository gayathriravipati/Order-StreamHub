package com.example.e_commerce_website.Config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.e_commerce_website.Model.DeadLetterMessage;
import com.example.e_commerce_website.Model.Order;
import com.example.e_commerce_website.Serializer.XmlSerializer;

@Configuration
public class KafkaProducerConfig {

	@Bean
    public ProducerFactory<Long, Order> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Long, Order> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
 // ➕ XML producer
    @Bean
    public ProducerFactory<Long, Order> xmlProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, XmlSerializer.class); // use correct class

        return new DefaultKafkaProducerFactory<>(
            configProps,
            new LongSerializer(),
            new XmlSerializer<>(Order.class)
        );
    }


    @Bean(name = "xmlKafkaTemplate")
    public KafkaTemplate<Long, Order> xmlKafkaTemplate() {
        return new KafkaTemplate<>(xmlProducerFactory());
    }

    
}
