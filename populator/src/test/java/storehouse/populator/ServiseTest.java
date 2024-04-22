package storehouse.populator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import storehouse.populator.config.ProviderConfig;
import storehouse.populator.dto.StoreDataDto;
import storehouse.populator.service.PopulatorService;

@SpringBootTest
@Slf4j
public class ServiseTest {

	@MockBean
	RestTemplate restTemplate;
	@Autowired
	PopulatorService populatorService;
	@Autowired
	ProviderConfig providerConfig;

	
	@Test
	void normalFlowTest() {
		Integer persentageInteger = 10;
		StoreDataDto storeDataDto = new StoreDataDto("A", persentageInteger);
		String url = getFullUrl(storeDataDto);
		
		ResponseEntity<Optional<Integer>> responseEntity =
				new ResponseEntity<Optional<Integer>>(Optional.of(persentageInteger), HttpStatus.OK);
		
		
		when(restTemplate.exchange(RequestEntity.put(url).body(null), new ParameterizedTypeReference<Optional<Integer>>(){}))
		.thenReturn(responseEntity);
		
		Optional<Integer> resOptional = populatorService.putCurrentPercentage(storeDataDto);
		assertEquals(resOptional.get(), persentageInteger);
		
	}
	
	
	
	

private String getFullUrl(StoreDataDto storeDataDto) {
	
	String res = String.format("http://%s:%d%s",
			providerConfig.getHost(),
			providerConfig.getPort(),
			String.format(
				providerConfig.getUrlTemplatePutNamePercentage(), storeDataDto.name(), storeDataDto.fillPercentage()
				)
			);
	log.debug("url:{} was created", res);
	return res;
	
	}
}
