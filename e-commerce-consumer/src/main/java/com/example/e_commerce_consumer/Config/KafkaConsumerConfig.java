package com.example.e_commerce_consumer.Config;

import com.example.e_commerce_consumer.Model.UsOrder;
import com.example.e_commerce_consumer.Model.UkOrder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private Map<String, Object> baseConfig(String groupId, Class<?> targetClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // Use ErrorHandlingDeserializer
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Delegates
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        // JsonDeserializer config
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, targetClass.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);  
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000); // 30s
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000); // 10s


        return props;
    }

    @Bean
    public ConsumerFactory<Long, UsOrder> usConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(baseConfig("us-consumer", UsOrder.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, UsOrder> usKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, UsOrder> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(usConsumerFactory());
        factory.setConcurrency(2);
        // Optional: log or handle deserialization failures instead of crashing
        //new FixedBackOff(0L, 2L) -- dealay btw each retry, max number of retries
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(0L, 2L)));
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, UkOrder> ukConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(baseConfig("uk-consumer", UkOrder.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, UkOrder> ukKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, UkOrder> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ukConsumerFactory());
        factory.setConcurrency(2);
        // Optional: retry or recover
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(0L, 2L)));

        return factory;
    }
    
    
    
    
}
