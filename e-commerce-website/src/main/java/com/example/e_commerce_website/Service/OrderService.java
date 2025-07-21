package com.example.e_commerce_website.Service;

import com.example.e_commerce_website.Dto.OrderRequest;
import com.example.e_commerce_website.Model.Order;
import com.example.e_commerce_website.Repository.OrderRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private OrderKafkaProducer orderKafkaProducer;

    public Order saveOrder(OrderRequest request) {
        double numericValue = request.getValue();

        Order order = new Order();
        order.setValue(numericValue);
        order.setAddress(request.getAddress());

        Order savedOrder = orderRepository.save(order);
        logger.info("🗃️ Saved order to database: {}", savedOrder);

        try {
            logger.info("🚀 Sending order to Kafka...");
            orderKafkaProducer.sendOrderWithRetry(savedOrder);
        } catch (Exception ex) {
            logger.error("⚠️ Kafka send failed for Order ID {}: {}", savedOrder.getId(), ex.getMessage(), ex);
        }

        return savedOrder;
    }
}
