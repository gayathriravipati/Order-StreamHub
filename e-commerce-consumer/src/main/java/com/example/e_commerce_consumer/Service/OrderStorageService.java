package com.example.e_commerce_consumer.Service;

import org.springframework.stereotype.Service;

import com.example.e_commerce_consumer.DTO.Order;
import com.example.e_commerce_consumer.Model.HighValueOrder;
import com.example.e_commerce_consumer.Model.LowValueOrder;
import com.example.e_commerce_consumer.Repository.HighValueOrderRepository;
import com.example.e_commerce_consumer.Repository.LowValueOrderRepository;

@Service
public class OrderStorageService {

    private final HighValueOrderRepository highRepo;
    private final LowValueOrderRepository lowRepo;

    public OrderStorageService(HighValueOrderRepository highRepo, LowValueOrderRepository lowRepo) {
        this.highRepo = highRepo;
        this.lowRepo = lowRepo;
    }

    public void saveOrder(Order order) {
        if (order.getAmount().compareTo(Double.valueOf(1000)) >= 0) {
            highRepo.save(convertToHigh(order));
        } else {
            lowRepo.save(convertToLow(order));
        }
    }

    private HighValueOrder convertToHigh(Order order) {
        HighValueOrder high = new HighValueOrder();
        high.setOrderId(order.getOrderId());
        high.setAmount(order.getAmount());
        high.setAddress(order.getAddress());
        return high;
    }

    private LowValueOrder convertToLow(Order order) {
        LowValueOrder low = new LowValueOrder();
        low.setOrderId(order.getOrderId());
        low.setAmount(order.getAmount());
        low.setAddress(order.getAddress());
        return low;
    }
}

