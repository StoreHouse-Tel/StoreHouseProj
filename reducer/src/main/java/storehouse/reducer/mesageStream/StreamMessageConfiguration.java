package storehouse.reducer.mesageStream;


import storehouse.reducer.dto.StoreDataDto;
import storehouse.reducer.service.ContainerOccupationService;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StreamMessageConfiguration {

	final private ContainerOccupationService containerOccupationService;
	final private StreamBridge streamBridge;
	final private Gson gson;
	@Value("${app.reducer.binding-output-name}" + "-out-0")
	String bindingName;
	
	
	@Bean
	Consumer<String> storeDataChangeOccupation() {
		log.debug("Bean Consumer storeDataChangeOccupation created");
		return (String storeDataString) -> {
			StoreDataDto storeDataDto = gson.fromJson(storeDataString, StoreDataDto.class); 
			log.trace("Received data from message broker: {}", storeDataDto);
			StoreDataDto updatedStoreDataDto = containerOccupationService.getChangedOccupationLevelFor(storeDataDto);
			if(updatedStoreDataDto != null) {
				log.trace("Container name: {} occupation was changed, new occupation level: {}", storeDataDto.name(), updatedStoreDataDto.fillPercentage());
				String sendingMessageString = gson.toJson(updatedStoreDataDto);
				streamBridge.send(bindingName, sendingMessageString);
				log.trace("The following data was sent: {}", sendingMessageString);
			} else {
				log.trace("Container name: {} occupation was not changed", storeDataDto.name());
			}
		};
	}
	
}

