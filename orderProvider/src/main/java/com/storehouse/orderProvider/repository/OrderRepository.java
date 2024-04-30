package com.storehouse.orderProvider.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.storehouse.orderProvider.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
	Optional<Order> findById(String id);
	Order findByContainerName(String containerName);
    void deleteByContainerName(String containerName);
    List<Order> findAll();
}