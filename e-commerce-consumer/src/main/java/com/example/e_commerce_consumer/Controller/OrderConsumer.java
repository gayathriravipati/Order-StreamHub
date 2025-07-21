package com.example.e_commerce_consumer.Controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.e_commerce_consumer.Model.UkOrder;
import com.example.e_commerce_consumer.Model.UsOrder;
import com.example.e_commerce_consumer.Repository.UkOrderRepository;
import com.example.e_commerce_consumer.Repository.UsOrderRepository;

@Component
public class OrderConsumer {

    @Autowired
    private UsOrderRepository usOrderRepository;
    @Autowired
    private UkOrderRepository ukOrderRepository;
    @KafkaListener(topics = "order-created-US", containerFactory = "usKafkaListenerFactory", groupId = "us-orders-group")
    public void consumeUsOrder(UsOrder payload, org.apache.kafka.clients.consumer.ConsumerRecord<Long, UsOrder> record) {
        UsOrder order = new UsOrder();
        order.setAddress(payload.getAddress());
        order.setAmount(payload.getAmount());
        System.out.printf(payload.getAddress() + " " + payload.getAmount());
        order.setOrderId(record.key());

        usOrderRepository.save(order);
        System.out.printf("✅ Saved US order: KafkaKey=%d, DB_ID=%s%n", record.key(), order.getId());
    }

    @KafkaListener(topics = "order-created-UK", containerFactory = "ukKafkaListenerFactory", groupId = "uk-orders-group")
    public void consumeUkOrder(UkOrder payload, org.apache.kafka.clients.consumer.ConsumerRecord<Long, UkOrder> record) {
        UkOrder order = new UkOrder();
        order.setAddress(payload.getAddress());
        order.setAmount(payload.getAmount());
        order.setOrderId(record.key());

        ukOrderRepository.save(order);
        System.out.printf("✅ Saved UK order: KafkaKey=%d, DB_ID=%s%n", record.key(), order.getId());
    }
    
    @KafkaListener(
            topics = { "order-created-US-xml", "order-created-UK-xml" },
            containerFactory = "xmlKafkaListenerFactory",
            groupId = "xml-multi-xml-orders-group"
        )
        public void consumeXmlOrder(String payload, ConsumerRecord<Long, String> record) {
            String topic = record.topic();
            String origin = topic.contains("US") ? "US" : topic.contains("UK") ? "UK" : "UNKNOWN";
            
            System.out.printf("📦 [%s XML] Received message: KafkaKey=%d, Payload=%s%n", origin, record.key(), payload);
        }
    }
    
    
