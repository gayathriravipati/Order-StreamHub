package com.example.e_commerce_consumer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.e_commerce_consumer.Model.UkOrder;
import com.example.e_commerce_consumer.Model.UsOrder;

public interface UkOrderRepository extends JpaRepository<UkOrder, Long> {

}
