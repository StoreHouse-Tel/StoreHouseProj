package storehouse.populator.service;

import java.util.Optional;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import storehouse.populator.config.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import storehouse.populator.dto.StoreDataDto;


@Service
@RequiredArgsConstructor
@Slf4j
public class PopulatorServiceImpl implements PopulatorService {

	final private ProviderConfig providerConfig;
	final private RestTemplate restTemplate;
	final private ObjectMapper objectMapper;
	
	
	@Override
	public Integer putCurrentPercentage(StoreDataDto storeDataDto) {
		log.trace("putCurrentPercentage method got storeDataDto: {}", storeDataDto);
		ResponseEntity<String> responseEntity = restTemplate.exchange(RequestEntity.put(getFullUrl(storeDataDto)).body(null),  String.class);
		Integer result;
		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			Optional<String> stringResult = Optional.ofNullable(responseEntity.getBody());
			log.trace("putCurrentPercentage method successful sent data to provider: {}", storeDataDto);
			 
			if(stringResult.isEmpty()) {
				log.error("Putting current percentage got nul from Provider");
				result = null;
			} else {
				try {
					result = objectMapper.readValue(stringResult.get(), Integer.class);
				} catch (JsonProcessingException e) {
					result = null;
					log.error(e.getMessage());
				}
				log.trace("Putting current percentage ended with result: {}", result);	
			}
				
		} else {
			result = null;
			String errorMessage = (String) responseEntity.getBody();
			log.error("putCurrentPercentage method faild to sending data to provider by the reason: {}", errorMessage);
        }
		return result;
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
