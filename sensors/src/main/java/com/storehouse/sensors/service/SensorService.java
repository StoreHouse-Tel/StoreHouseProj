package com.storehouse.sensors.service;


import com.storehouse.sensors.model.Sensor;
import com.storehouse.sensors.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SensorService {
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private KafkaSender kafkaSender;
    @Value("${app.enableConsumption}")
    Boolean enableConsumption;
    @Value("${app.enableAutofill}")
    Boolean enableAutofill;
    public void processAndSendSensorData() {
        log.info("Starting to process sensor data");
        List<Sensor> sensors = sensorRepository.findAll();
        if (sensors.isEmpty()) {
            log.info("No sensors found in database");
        } else {
            sensors.forEach(sensor -> {
            	
            	 if(enableConsumption) {
                  	int currentQuantity = sensor.getQuantity();
                  	if(currentQuantity > 0) {
                  		sensor.setQuantity(currentQuantity - 1);
                  		sensorRepository.save(sensor);
                  	} else if (enableAutofill) {
                  		sensor.setQuantity(sensor.getMaxCapacity());
                  		sensorRepository.save(sensor);
  					}
                 }
            	
                log.info("quantity: {} copacity: {}", sensor.getQuantity(),sensor.getMaxCapacity());
                double fillPercentage = (double) sensor.getQuantity() / sensor.getMaxCapacity() * 100;
                log.info("percentage {}",fillPercentage);
                String message = String.format("{\"name\":\"%s\", \"fillPercentage\":%d}", sensor.getName(),Math.round(fillPercentage));
                log.info("Sending message: {}", message);
                kafkaSender.sendMessage("sensor-topic", message);
              
            });
    }
}}