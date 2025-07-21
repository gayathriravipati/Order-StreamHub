package com.example.e_commerce_website.Model;

import java.time.LocalDateTime;

public class DeadLetterMessage {
    private Long originalOrderId;
    private Order order;
    private String failureReason;
    private LocalDateTime failedAt;

    public DeadLetterMessage() {}

    public DeadLetterMessage(Long originalOrderId, Order order, String failureReason, LocalDateTime failedAt) {
        this.originalOrderId = originalOrderId;
        this.order = order;
        this.failureReason = failureReason;
        this.failedAt = failedAt;
    }

    // Getters and setters
    public Long getOriginalOrderId() {
        return originalOrderId;
    }

    public void setOriginalOrderId(Long originalOrderId) {
        this.originalOrderId = originalOrderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(LocalDateTime failedAt) {
        this.failedAt = failedAt;
    }
}
