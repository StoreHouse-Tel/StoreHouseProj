package storehouse.reducer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import storehouse.reducer.dto.StoreDataDto;
import storehouse.reducer.model.StoreData;
import storehouse.reducer.repo.StoreDataRepo;
import storehouse.reducer.service.ContainerOccupationService;

@SpringBootTest
public class ServiceTest {
	@Autowired
	ContainerOccupationService containerOccupationService;	
	@MockBean
	StoreDataRepo storeDataRepo;
	Map<String, Integer> testMap = null;
	
	final String STORE_ID_IN_REDIS_RECORD = "A";
	final StoreData STORE_DATA_IN_REDIS_RECORD = StoreData.fromDto(new StoreDataDto(STORE_ID_IN_REDIS_RECORD, 10));
	final StoreData UPDATED_STORE_DATA_IN_REDIS_RECORD = StoreData.fromDto(new StoreDataDto(STORE_ID_IN_REDIS_RECORD, 20));
	
	
	
	final String STORE_ID_NO_REDIS_RECORD = "F";
	final StoreData STORE_DATA_NO_REDIS_RECORD = StoreData.fromDto(new StoreDataDto(STORE_ID_NO_REDIS_RECORD, 10));
	
	
	
	@BeforeEach
	void setup() {
		testMap = new HashMap<String, Integer>(Map.of("A", 10, "B", 20, "C", 100, "D", 50));
	}
	
	@Test
	void testNoDataInDB() {
		when(storeDataRepo.findById(STORE_ID_NO_REDIS_RECORD)).thenReturn(Optional.ofNullable(null));
		when(storeDataRepo.save(STORE_DATA_NO_REDIS_RECORD)).thenAnswer(new Answer<StoreData>() {
			@Override
			public StoreData answer(InvocationOnMock invocation) throws Throwable {
				StoreDataDto invocationStoreData = ((StoreData) invocation.getArgument(0)).toDto(); 
				testMap.put(invocationStoreData.name(), invocationStoreData.fillPercentage());
				return invocation.getArgument(0);
			}
		});

		StoreDataDto returnedStoreDataDto = containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_NO_REDIS_RECORD.toDto());
		assertEquals(returnedStoreDataDto, STORE_DATA_NO_REDIS_RECORD.toDto());
		assertEquals(returnedStoreDataDto, new StoreDataDto(STORE_ID_NO_REDIS_RECORD, testMap.get(STORE_ID_NO_REDIS_RECORD)));
	}
	
	@Test
	void testSameDataInDB() {
		when(storeDataRepo.findById(STORE_ID_IN_REDIS_RECORD)).thenReturn(Optional.ofNullable(STORE_DATA_IN_REDIS_RECORD));
		verify(storeDataRepo, never()).save(STORE_DATA_IN_REDIS_RECORD);
		StoreDataDto returnedStoreDataDto = containerOccupationService.getChangedOccupationLevelFor(STORE_DATA_IN_REDIS_RECORD.toDto());
		assertNull(returnedStoreDataDto);
		
	}
	
	@Test
	void testDifferentOccupationDataInDB() {
		when(storeDataRepo.findById(STORE_ID_IN_REDIS_RECORD)).thenReturn(Optional.ofNullable(STORE_DATA_IN_REDIS_RECORD));
		when(storeDataRepo.save(UPDATED_STORE_DATA_IN_REDIS_RECORD)).thenAnswer(new Answer<StoreData>() {
			@Override
			public StoreData answer(InvocationOnMock invocation) throws Throwable {
				StoreDataDto invocationStoreData = ((StoreData) invocation.getArgument(0)).toDto(); 
				testMap.put(invocationStoreData.name(), UPDATED_STORE_DATA_IN_REDIS_RECORD.toDto().fillPercentage());
				return invocation.getArgument(0);
			}
		});

		StoreDataDto returnedStoreDataDto = containerOccupationService.getChangedOccupationLevelFor(UPDATED_STORE_DATA_IN_REDIS_RECORD.toDto());
		assertEquals(returnedStoreDataDto, UPDATED_STORE_DATA_IN_REDIS_RECORD.toDto());
		assertEquals(returnedStoreDataDto, new StoreDataDto(STORE_ID_IN_REDIS_RECORD, testMap.get(STORE_ID_IN_REDIS_RECORD)));
	}
	
}



