package com.storehouse.ContainerProvider.service;



import org.springframework.stereotype.Service;

import com.storehouse.ContainerProvider.model.Container;
import com.storehouse.ContainerProvider.repository.ContainerRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
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

    public Optional<Integer> getContainerFillPercentage(String name) { 
        return getContainerByName(name)
                .map(container -> container.getQuantity() * 100 / container.getMaxCapacity()); 
    }

    public Optional<Integer> containerLackQuantity(String name) {
        return getContainerByName(name)
                .map(container -> container.getMaxCapacity() - container.getQuantity());
    }
    
    public Optional<Integer> changePopulatorCurrentPercentage(String name, int percentage) {
    	log.info("Container : -> {} ,new occupancy level : -> {}",name , percentage);
        return getContainerByName(name)
                .map(container -> {
                    container.setPopulatorCurrentPercentage(percentage);
                    return repository.save(container).getPopulatorCurrentPercentage();
                });
    }

	public Optional<Integer> getPopulatorCurrentPercentage(String name) {
		
		return getContainerByName(name).map(Container::getPopulatorCurrentPercentage);
	}
}