package com.storehouse.orderProvider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.storehouse.orderProvider.model.Order;
import com.storehouse.orderProvider.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody Order order) {
    	log.info("create order start for container {}",order.getContainerName());
        return orderService.createOrder(order);
    }

    @DeleteMapping("/remove/{containerName}")
    public void deleteOrder(@PathVariable String containerName) {
        orderService.deleteOrder(containerName);
        log.info(" order was deleted");
    }

    @GetMapping("/{containerName}")
    public Order checkOrderExists(@PathVariable String containerName) {
        return orderService.checkOrderExists(containerName);
    }
    @PutMapping("/edit/{containerName}")
    public String editOrder(@PathVariable String containerName, @RequestBody Order order) {
        log.info("Edit order start for container {}", containerName);
        order.setContainerName(containerName); 
        return orderService.editOrder(order);
    }
    
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    
    
}