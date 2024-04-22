package com.storehouse.sensors.repository;


import com.storehouse.sensors.model.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SensorRepository extends MongoRepository<Sensor, String> {
}