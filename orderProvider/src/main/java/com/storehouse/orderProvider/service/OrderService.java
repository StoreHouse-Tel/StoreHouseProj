package com.storehouse.orderProvider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storehouse.orderProvider.model.Order;
import com.storehouse.orderProvider.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public  String createOrder(Order order) {
    	boolean checkOrderExist = checkOrderExists(order.getContainerName());
    	if(checkOrderExist) {
    		log.info("the order already exists in the database for container {}",order.getContainerName());
    		return "order already exists";
    	}else {
    		Order newOrder = new Order();
    		newOrder.setContainerName(order.getContainerName());
    		newOrder.setQuantity(order.getQuantity());
            orderRepository.save(newOrder);	
    	}
        
        return "order created successfully";
    }

    public void deleteOrder(String containerName){
        orderRepository.deleteByContainerName(containerName);
    }

    public boolean checkOrderExists(String containerName) {
        return orderRepository.findByContainerName(containerName) != null;
    }
    
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}