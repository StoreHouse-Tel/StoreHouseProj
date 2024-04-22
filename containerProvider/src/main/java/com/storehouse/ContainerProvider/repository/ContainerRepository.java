package com.storehouse.ContainerProvider.repository;



import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.storehouse.ContainerProvider.model.Container;

public interface ContainerRepository extends MongoRepository<Container, String> {
    Optional<Container> findByName(String name);
}