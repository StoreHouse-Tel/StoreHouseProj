package storehouse.populator;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import storehouse.populator.dto.StoreDataDto;
import storehouse.populator.service.PopulatorService;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@Slf4j
public class MessageStreaTest {

	@MockBean
	PopulatorService populatorService;
	@Autowired
	InputDestination inputStream;
	@Autowired
	ObjectMapper objectMapper;
	
	String inputBindingName = "storeDataPopulatorHandler-in-0";
	static private String STORE_DATA_JSON = String.format("{\"name\":\"%s\",\"fillPercentage\":%d}", "A", 10);

	
		
	@Test
	void testDataReseiving() {
		inputStream.send(new GenericMessage<String>(STORE_DATA_JSON),
				inputBindingName);
		try {
			verify(populatorService).putCurrentPercentage(objectMapper.readValue(STORE_DATA_JSON, StoreDataDto.class));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
	
	}
	
}
