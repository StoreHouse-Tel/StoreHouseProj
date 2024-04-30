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
    	 Order checkOrderExist = checkOrderExists(order.getContainerName());
         if (checkOrderExist != null) {
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

    public Order checkOrderExists(String containerName) {
        return orderRepository.findByContainerName(containerName);
    }
    
    public String editOrder(Order order) {
        // Check if the order exists
        Order checkOrderExist = checkOrderExists(order.getContainerName());
        if (checkOrderExist == null) {
            log.info("The order does not exist in the database for container {}", order.getContainerName());
            return "Order does not exist";
        } else {
            // Get the existing order
            Order existingOrder = orderRepository.findByContainerName(order.getContainerName());
            // Update the fields
            existingOrder.setQuantity(order.getQuantity());
            // Save the updated order
            orderRepository.save(existingOrder);
            log.info("Order updated successfully for container {}", order.getContainerName());
        }
        return "Order updated successfully";
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}