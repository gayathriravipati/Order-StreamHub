package com.example.e_commerce_consumer.Service;

import java.util.List;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.e_commerce_consumer.DTO.Order;
import com.example.e_commerce_consumer.Model.BaseOrder;
import com.example.e_commerce_consumer.Model.UkOrder;
import com.example.e_commerce_consumer.Model.UsOrder;

@Configuration
public class OrderStreamProcessor {

    private final OrderStorageService storageService;

    public OrderStreamProcessor(OrderStorageService storageService) {
        this.storageService = storageService;
    }

    @Bean
    public KStream<Long, BaseOrder> processOrders(StreamsBuilder builder) {
        JsonSerde<UkOrder> ukOrderSerde = new JsonSerde<>(UkOrder.class);
        JsonSerde<UsOrder> usOrderSerde = new JsonSerde<>(UsOrder.class);

        // One stream for each topic, each deserialized into its proper class
        KStream<Long, UkOrder> ukStream = builder.stream(
            "order-created-UK", Consumed.with(Serdes.Long(), ukOrderSerde)
        );

        KStream<Long, UsOrder> usStream = builder.stream(
            "order-created-US", Consumed.with(Serdes.Long(), usOrderSerde)
        );

        // Merge both streams into a unified BaseOrder stream
        KStream<Long, BaseOrder> unifiedStream = ukStream
            .mapValues(v -> (BaseOrder) v)
            .merge(usStream.mapValues(v -> (BaseOrder) v));

        // Convert to common Order object and pass to storageService
        unifiedStream.foreach((key, baseOrder) -> {
            Order common = new Order();
            common.setOrderId(key);
            common.setAmount(baseOrder.getAmount());
            common.setAddress(baseOrder.getAddress());
            storageService.saveOrder(common);
        });

        return unifiedStream;
    }
}
