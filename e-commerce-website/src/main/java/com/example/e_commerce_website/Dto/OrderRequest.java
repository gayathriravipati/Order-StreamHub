package com.example.e_commerce_website.Dto;

public class OrderRequest {
	private Double value;
    private String address;

    public OrderRequest() {}

    public OrderRequest(Double value, String address) {
        this.value = value;
        this.address = address;
    }

    // Getters and Setters

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
