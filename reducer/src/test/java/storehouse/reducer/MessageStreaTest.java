package storehouse.reducer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
	
	@Value("${app.reducer.binding-output-name}" + "-out-0")
	String outputBindingName;
	//@Value("${app.reducer.binding_name.input_changed_data}" + "-in-0")
	String inputBindingName = "storeDataChangeOccupation-in-0";

	
	final StoreDataDto STORE_DATA_NOT_CHANGED = new StoreDataDto("A", 10.);
	final StoreDataDto STORE_DATA_CHANGED = new StoreDataDto("B", 50.);
	final StoreDataDto STORE_DATA_NEW = new StoreDataDto("C", 100.);
	
	
	@BeforeEach
	void setUp() {
		when(containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_NOT_CHANGED)).thenReturn(null);
		when(containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_NEW)).thenReturn(STORE_DATA_NEW);
		when(containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_CHANGED)).thenReturn(STORE_DATA_CHANGED);
	}
	
	
	@Test
	void testNoSendingValue() {
		inputStream.send(new GenericMessage<StoreDataDto>(STORE_DATA_NOT_CHANGED),
				inputBindingName);
		Message<byte[]> message = outputStream.receive(10, outputBindingName);
		assertNull(message);
	}
	
	
	@Test
	void testSendNewData() throws Exception{
		System.out.println("inputBindingName: " + inputBindingName);
		System.out.println("outputBindingName: " + outputBindingName);
		when(containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_NEW)).thenReturn(STORE_DATA_NEW);
		inputStream.send(new GenericMessage<StoreDataDto>(STORE_DATA_NEW), inputBindingName);
		Message<byte[]> message = outputStream.receive(100, outputBindingName);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		StoreDataDto actual = mapper.readValue(message.getPayload(), StoreDataDto.class);
		assertEquals(STORE_DATA_NEW, actual);
		
	}
	
	@Test
	void testSendUpdatedData() throws Exception{
		inputStream.send(new GenericMessage<StoreDataDto>(STORE_DATA_CHANGED), inputBindingName);
		Message<byte[]> message = outputStream.receive(10, outputBindingName);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		StoreDataDto actual = mapper.readValue(message.getPayload(), StoreDataDto.class);
		assertEquals(STORE_DATA_CHANGED, actual);
		
	}
	
	
}
