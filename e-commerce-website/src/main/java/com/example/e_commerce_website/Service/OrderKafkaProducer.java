package com.example.e_commerce_website.Service;

import com.example.e_commerce_website.Model.Order;
import com.example.e_commerce_website.Model.DeadLetterMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.*;

@Service
public class OrderKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(OrderKafkaProducer.class);

    @Autowired
    private KafkaTemplate<Long, Order> kafkaTemplate; // JSON

    @Autowired
    private KafkaTemplate<Long, DeadLetterMessage> deadLetterKafkaTemplate; // JSON
    
    @Autowired
    private KafkaTemplate<Long, Order> xmlKafkaTemplate; // XML

    @Autowired
    private KafkaTemplate<Long, DeadLetterMessage> deadLetterXmlKafkaTemplate; // XML

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public void sendOrderWithRetry(Order order) {
        // Determine JSON and XML topics based on address
        String baseTopic = order.getAddress().equalsIgnoreCase("US") ? "order-created-US" : "order-created-UK";
        String jsonTopic = baseTopic;
        String xmlTopic = baseTopic + "-xml";

        logger.debug("💬 Sending Order ID {} to both JSON and XML topics", order.getId());

        // Send both formats in parallel
        sendWithRetry(order, jsonTopic, 0, false); // JSON
        sendWithRetry(order, xmlTopic, 0, true);   // XML
    }

    private void sendWithRetry(Order order, String topic, int attempt, boolean isXmlFormat) {
        try {
            KafkaTemplate<Long, Order> template = isXmlFormat ? xmlKafkaTemplate : kafkaTemplate;

            template.send(topic, order.getId(), order)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        RecordMetadata metadata = result.getRecordMetadata();
                        logger.info("✅ Order ID {} sent to Kafka topic '{}' (partition {}, offset {})",
                                order.getId(), metadata.topic(), metadata.partition(), metadata.offset());
                    } else {
                        handleSendFailure(order, topic, attempt, ex, isXmlFormat);
                    }
                });
        } catch (Exception e) {
            logger.error("🚨 Immediate send failure for Order ID {} to topic '{}': {}", order.getId(), topic, e.getMessage(), e);
            handleSendFailure(order, topic, attempt, e, isXmlFormat);
        }
    }

    private void handleSendFailure(Order order, String topic, int attempt, Throwable ex, boolean isXmlFormat) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;

        if (cause instanceof UnknownTopicOrPartitionException) {
            logger.error("🚫 Topic '{}' not found in Kafka broker for Order ID {}", topic, order.getId());
        }

        logger.warn("❌ Exception occurred for Order ID {} on topic '{}': {}", order.getId(), topic, cause.toString(), cause);

        if (attempt < MAX_RETRIES) {
            logger.info("🔁 Retrying Order ID {} to topic '{}' (attempt {} of {})", order.getId(), topic, attempt + 1, MAX_RETRIES);
            scheduler.schedule(() ->
                            sendWithRetry(order, topic, attempt + 1, isXmlFormat),
                    RETRY_DELAY_MS, TimeUnit.MILLISECONDS);
        } else {
            logger.error("💀 Max retries reached for Order ID {} to topic '{}'. Sending to Dead Letter Topic.", order.getId(), topic);
            sendToDeadLetterTopic(order, cause.getMessage(), isXmlFormat);
        }
    }

    private void sendToDeadLetterTopic(Order order, String reason, boolean isXmlFormat) {
        DeadLetterMessage dlm = new DeadLetterMessage(
                order.getId(),
                order,
                reason,
                LocalDateTime.now()
        );

        KafkaTemplate<Long, DeadLetterMessage> deadLetterTemplate = isXmlFormat
                ? deadLetterXmlKafkaTemplate
                : deadLetterKafkaTemplate;

        String dlTopic = isXmlFormat ? "order-dead-letter-xml" : "order-dead-letter";

        deadLetterTemplate.send(dlTopic, order.getId(), dlm)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        logger.error("❌ Failed to send to Dead Letter Topic '{}' for Order ID {}: {}",
                                dlTopic, order.getId(), ex.getMessage(), ex);
                    } else {
                        RecordMetadata metadata = result.getRecordMetadata();
                        logger.info("📦 Sent Order ID {} to Dead Letter Topic '{}'", order.getId(), metadata.topic());
                    }
                });
    }
}
