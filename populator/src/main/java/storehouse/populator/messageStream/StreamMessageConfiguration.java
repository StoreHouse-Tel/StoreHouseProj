package storehouse.populator.messageStream;

import java.io.IOException;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import storehouse.populator.dto.StoreDataDto;
import storehouse.populator.service.PopulatorService;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StreamMessageConfiguration {

	final private PopulatorService populatorService;
	final private ObjectMapper objectMapper;
	@Bean
	Consumer<String> storeDataPopulatorHandler() {
		log.debug("Bean Consumer storeDataPopulatorHandler created");
		return (String json) -> {
			try {
				StoreDataDto storeDataDto = objectMapper.readValue(json, StoreDataDto.class);
				log.trace("Received data from message broker: {}", storeDataDto);
				populatorService.putCurrentPercentage(storeDataDto);
			} catch (Exception e) {
				log.error("Exception while reading the reseived message: {}", e.getMessage());
				e.printStackTrace();
			} 
	
		};
	}
}

