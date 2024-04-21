package storehouse.reducer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import storehouse.reducer.dto.StoreDataDto;
import storehouse.reducer.repo.StoreDataRepo;
import storehouse.reducer.service.ContainerOccupationService;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class MessageStreaTest {

	@MockBean
	ContainerOccupationService containerOccupationService;
	@MockBean
	StoreDataRepo storeDataRepo;
	@Autowired
	InputDestination inputStream;
	@Autowired
	OutputDestination outputStream;
	@Autowired
	Gson gson;
	
	@Value("${app.reducer.binding-output-name}" + "-out-0")
	String outputBindingName;
	//@Value("${app.reducer.binding_name.input_changed_data}" + "-in-0")
	String inputBindingName = "storeDataChangeOccupation-in-0";

	
	static private String STORE_DATA_NOT_CHANGED_STRING = String.format("{\"name\":\"%s\",\"fillPercentage\":%d}", "A", 10);
	static private String STORE_DATA_CHANGED_STRING = String.format("{\"name\":\"%s\",\"fillPercentage\":%d}", "B", 50);
	static private String STORE_DATA_NEW_STRING  = String.format("{\"name\":\"%s\",\"fillPercentage\":%d}", "C", 100);
	
	static private StoreDataDto STORE_DATA_NOT_CHANGED;
	static private StoreDataDto STORE_DATA_CHANGED;
	static private StoreDataDto STORE_DATA_NEW;
	
	
	@BeforeEach
	void setUp() {

		STORE_DATA_NOT_CHANGED = gson.fromJson(STORE_DATA_NOT_CHANGED_STRING, StoreDataDto.class);
		STORE_DATA_CHANGED = gson.fromJson(STORE_DATA_CHANGED_STRING, StoreDataDto.class);
		STORE_DATA_NEW =  gson.fromJson(STORE_DATA_NEW_STRING, StoreDataDto.class);
		
		when(containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_NOT_CHANGED)).thenReturn(null);
		when(containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_NEW)).thenReturn(STORE_DATA_NEW);
		when(containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_CHANGED)).thenReturn(STORE_DATA_CHANGED);
			
	}
	
	
	@Test
	void testNoSendingValue() {
		inputStream.send(new GenericMessage<String>(STORE_DATA_NOT_CHANGED_STRING),
				inputBindingName);
		Message<byte[]> message = outputStream.receive(10, outputBindingName);
		assertNull(message);
	}
	
	
	@Test
	void testSendNewData() throws Exception{
		inputStream.send(new GenericMessage<String>(STORE_DATA_NEW_STRING), inputBindingName);
		Message<byte[]> message = outputStream.receive(100, outputBindingName);
		assertNotNull(message);
		String actual = new String(message.getPayload(), StandardCharsets.UTF_8);
		assertEquals(STORE_DATA_NEW_STRING, actual);
		
	}
	
	@Test
	void testSendUpdatedData() throws Exception{
//		inputStream.send(new GenericMessage<String>(STORE_DATA_CHANGED_STRING), inputBindingName);
//		Message<byte[]> message = outputStream.receive(10, outputBindingName);
//		assertNotNull(message);
//		ObjectMapper mapper = new ObjectMapper();
//		String actual = mapper.readValue(message.getPayload(), String.class);
//		assertEquals(STORE_DATA_CHANGED_STRING, actual);
		
	}
	
	
}
