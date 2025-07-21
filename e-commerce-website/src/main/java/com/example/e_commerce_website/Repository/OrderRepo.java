package com.example.e_commerce_website.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.e_commerce_website.Model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>{
	
}
