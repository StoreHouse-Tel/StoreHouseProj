package com.storehouse.ContainerProvider.controller;


import com.storehouse.ContainerProvider.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/container")
public class ContainerController {
    @Autowired
    private ContainerService service;

    @GetMapping("/{name}")
    public Object getContainerByName(@PathVariable String name) {
        return service.getContainerByName(name);
    }

    @GetMapping("/{name}/threshold")
    public Object getContainerThreshold(@PathVariable String name) {
        return service.getContainerThreshold(name);
    }

    @GetMapping("/{name}/fill-percentage")
    public Object getContainerFillPercentage(@PathVariable String name) {
        return service.getContainerFillPercentage(name);
    }

    @GetMapping("/{name}/lack-quantity")
    public Object containerLackQuantity(@PathVariable String name) {
        return service.containerLackQuantity(name);
    }
}