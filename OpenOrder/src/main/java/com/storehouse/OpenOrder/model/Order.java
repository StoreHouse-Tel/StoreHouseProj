package com.storehouse.OpenOrder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    
    
    private String containerName;
    private int quantity;
}