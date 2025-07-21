package com.example.e_commerce_website.Controller;

import com.example.e_commerce_website.Dto.OrderRequest;
import com.example.e_commerce_website.Model.Order;
import com.example.e_commerce_website.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        logger.info("📥 Received new order request: {}", request);

        if (request.getAddress() == null ||
                (!request.getAddress().equalsIgnoreCase("US") &&
                        !request.getAddress().equalsIgnoreCase("UK"))) {
            logger.warn("❗Invalid address: {}", request.getAddress());
            return ResponseEntity
                    .badRequest()
                    .body("Invalid address. Only 'US' or 'UK' are accepted.");
        }

        try {
            double numericValue = request.getValue();
            if (numericValue <= 0) {
                logger.warn("❗Invalid value: {}", numericValue);
                return ResponseEntity
                        .badRequest()
                        .body("Invalid value. Must be a positive number.");
            }
        } catch (NumberFormatException e) {
            logger.error("❗Invalid value format", e);
            return ResponseEntity
                    .badRequest()
                    .body("Invalid value. Must be a numeric string.");
        }

        Order savedOrder = orderService.saveOrder(request);
        logger.info("✅ Order created successfully: {}", savedOrder);

        return ResponseEntity.ok(savedOrder);
    }
}
