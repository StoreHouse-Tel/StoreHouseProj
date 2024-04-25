package com.storehouse.AutoFill.service;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.storehouse.AutoFill.model.*;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class autoFillService {

	
	@Autowired
    private RestTemplate restTemplate;
	

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
    private ObjectMapper objectMapper = new ObjectMapper();
	
	  @KafkaListener(topics = "threshold-alert")
	    public void consumeContainerData(String message) {
		  
		  ContainerData containerData;
		try {
			containerData = objectMapper.readValue(message, ContainerData.class);
			log.info("Autofill -> start consume data {}",containerData);


	        
	        ResponseEntity<Integer> response = restTemplate.getForEntity("http://localhost:8082/api/container/{containerName}/lack-quantity", Integer.class, containerData.containerName());
	        int lackQuantity = response.getBody();
	        log.info("lack quantity for container {} is -> {}",containerData.containerName(),lackQuantity);
	        String newMessage = String.format("{\"containerName\":\"%s\", \"quantity\":%d}", containerData.containerName(),lackQuantity);
	        kafkaTemplate.send("create-order", newMessage);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	        
	    }
}
