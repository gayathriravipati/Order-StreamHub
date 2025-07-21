package com.example.e_commerce_consumer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.e_commerce_consumer.Model.HighValueOrder;

@Repository
public interface HighValueOrderRepository extends JpaRepository<HighValueOrder, Long> {
}
