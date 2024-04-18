package com.storehouse.sensors.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "containers")
public class Sensor {
    @Id
    private String id;
    private String name;
    private String unit;
    private int maxCapacity;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int threshold;

}