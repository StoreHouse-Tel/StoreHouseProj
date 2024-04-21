package com.storehouse.analyzer.service;



import com.storehouse.analyzer.model.ItemUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class AnalyzerService {
	@Autowired
    private RestTemplate restTemplate;
	

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "sensor-topic", groupId = "my-group")
    public void listenItemUpdates(String message) {
        try {
            ItemUpdate itemUpdate = objectMapper.readValue(message, ItemUpdate.class);
            log.info("message-> {}",itemUpdate.name() );
            int threshold = restTemplate.getForObject("http://localhost:8082/api/container/{name}/threshold", Integer.class, itemUpdate.name());

          
            if (itemUpdate != null && threshold  < itemUpdate.fillPercentage()) {
                log.info("container-> {} threshold -> {}",itemUpdate.name() ,itemUpdate.fillPercentage());
                kafkaTemplate.send("Threshold-alert", "ContainerName: " + itemUpdate.name() + " ThresholdAlert: "+ threshold);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
