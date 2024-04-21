package com.storehouse.sensors.controller;



import com.storehouse.sensors.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.web.bind.annotation.RestController;

@RestController

public class SensorController {
    @Autowired
    private SensorService sensorService;

//    @Bean
    @Scheduled(fixedRate = 5000)
    public String processSensors() {
        sensorService.processAndSendSensorData();
        return "Data processed and sent to Kafka successfully!";
    }
}