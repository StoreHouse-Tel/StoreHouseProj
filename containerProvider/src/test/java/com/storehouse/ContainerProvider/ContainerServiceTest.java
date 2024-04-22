package com.storehouse.ContainerProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storehouse.ContainerProvider.model.Container;
import com.storehouse.ContainerProvider.repository.ContainerRepository;
import com.storehouse.ContainerProvider.service.ContainerService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ContainerServiceTest {

    @Mock
    private ContainerRepository containerRepository;

    @InjectMocks
    private ContainerService containerService;

    @Test
    void testGetContainerByName() {
        Container container = new Container();
        container.setName("A1");
        when(containerRepository.findByName("A1")).thenReturn(Optional.of(container));

        Optional<Container> result = containerService.getContainerByName("A1");
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getName()).isEqualTo("A1");
    }

    @Test
    void testGetContainerThreshold() {
        Container container = new Container();
        container.setThreshold(47);
        when(containerRepository.findByName("A1")).thenReturn(Optional.of(container));

        Optional<Integer> result = containerService.getContainerThreshold("A1");
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(47);
    }

    @Test
    void testGetContainerFillPercentage() {
        Container container = new Container();
        container.setQuantity(200);
        container.setMaxCapacity(400);
        when(containerRepository.findByName("A1")).thenReturn(Optional.of(container));

        Optional<Integer> result = containerService.getContainerFillPercentage("A1");
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(50);
    }

    @Test
    void testContainerLackQuantity() {
        Container container = new Container();
        container.setQuantity(150);
        container.setMaxCapacity(500);
        when(containerRepository.findByName("A1")).thenReturn(Optional.of(container));

        Optional<Integer> result = containerService.containerLackQuantity("A1");
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(350);
    }
}
