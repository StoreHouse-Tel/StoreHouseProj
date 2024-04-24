package com.storehouse.OpenOrder.service;


import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.storehouse.OpenOrder.model.Order;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class OpenOrderService {
	
	@Autowired
    private RestTemplate restTemplate;
	

    
	
	
	 @KafkaListener(topics = "create-order", groupId = "orderGroup")
	    public void consumeAndCreateOrder(Order order) {
	        
		 String url = "http://localhost:8086/orders";
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<Order> requestEntity = new HttpEntity<>(order, headers);

	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

	        if (response.getStatusCode() == HttpStatus.CREATED) {
	            log.info("OpenOrder - Order created successfully");
	        } else {
	        	 log.info("Failed to create order. Response code: " + response.getStatusCode());
	        }
	        
	       
	    }
}
