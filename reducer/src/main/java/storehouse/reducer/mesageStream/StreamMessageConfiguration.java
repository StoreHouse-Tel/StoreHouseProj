package storehouse.reducer.mesageStream;

import storehouse.reducer.SensorDataReducerApplication;
import storehouse.reducer.dto.StoreDataDto;
import storehouse.reducer.service.ContainerOccupationService;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StreamMessageConfiguration {

	final private ContainerOccupationService containerOccupationService;
	final private StreamBridge streamBridge;
	
	@Value("${app.reducer.binding-output-name}" + "-out-0")
	String bindingName;
	
	
	@Bean
	Consumer<StoreDataDto> storeDataChangeOccupation() {
		log.debug("Bean Consumer storeDataChangeOccupation created");
		return (StoreDataDto storeDataDto) -> {
			log.trace("Received data from message broker: {}", storeDataDto);
			StoreDataDto updatedStoreDataDto = containerOccupationService.getChangedOccupationLevelFor(storeDataDto);
			if(updatedStoreDataDto != null) {
				log.debug("Container id: {} occupation was changed, new occupation level: {}", storeDataDto.id(), updatedStoreDataDto.occupation());
				streamBridge.send(bindingName, updatedStoreDataDto);
			} else {
				log.trace("Container id: {} occupation was not changed", storeDataDto.id());
			}
		};
	}
	
}

