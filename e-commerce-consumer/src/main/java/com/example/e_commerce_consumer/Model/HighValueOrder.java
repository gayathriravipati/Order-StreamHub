package com.example.e_commerce_consumer.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "high_value_orders")
public class HighValueOrder extends BaseOrder {
    // No extra fields required
}