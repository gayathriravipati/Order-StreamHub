package com.example.e_commerce_consumer.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "low_value_orders")
public class LowValueOrder extends BaseOrder {

}