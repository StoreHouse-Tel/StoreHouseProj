package com.storehouse.ContainerProvider.service;



import org.springframework.stereotype.Service;

import com.storehouse.ContainerProvider.model.Container;
import com.storehouse.ContainerProvider.repository.ContainerRepository;

import java.util.Optional;

@Service
public class ContainerService {
    private final ContainerRepository repository;

    public ContainerService(ContainerRepository repository) {
        this.repository = repository;
    }

    public Optional<Container> getContainerByName(String name) {
        return repository.findByName(name);
    }

    public Optional<Integer> getContainerThreshold(String name) {
        return getContainerByName(name).map(Container::getThreshold);
    }

    public Optional<Double> getContainerFillPercentage(String name) {
        return getContainerByName(name)
                .map(container -> 100.0 * container.getQuantity() / container.getMaxCapacity());
    }

    public Optional<Integer> containerLackQuantity(String name) {
        return getContainerByName(name)
                .map(container -> container.getMaxCapacity() - container.getQuantity());
    }
}