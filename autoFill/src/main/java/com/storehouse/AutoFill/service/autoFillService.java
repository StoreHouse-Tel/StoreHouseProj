package com.storehouse.AutoFill.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import com.storehouse.AutoFill.model.*;
public class autoFillService {

	
	@Autowired
    private RestTemplate restTemplate;
	

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
	
	
	  @KafkaListener(topics = "threshold-alert")
	    public void consumeContainerData(ContainerData containerData) {
	        
	        String containerName = containerData.containerName();
//	        int percentage = containerData.percentage();

	        
	        ResponseEntity<Integer> response = restTemplate.getForEntity("http:loclahost:8082/api/container/{containerName}/lack-quantity", Integer.class, containerName);
	        int lackQuantity = response.getBody();

	       
	        kafkaTemplate.send("create-order", new LackQuantity(containerName, lackQuantity).toString());
	    }
}
