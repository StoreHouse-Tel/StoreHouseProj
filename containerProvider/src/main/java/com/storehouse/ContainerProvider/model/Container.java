package com.storehouse.ContainerProvider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "containers")
public class Container {
    @Id
    private String id;
    private String name;
    private String unit;
    private int maxCapacity;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int threshold;
    private int populatorCurrentPercentage;
}