package com.example.e_commerce_consumer.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;

public class Order {
    private Long orderId;
    private String address;
    @JsonProperty("value")
    private Double amount;
    

    public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
