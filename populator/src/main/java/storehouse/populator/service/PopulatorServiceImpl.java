package storehouse.populator.service;

import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import storehouse.populator.config.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import storehouse.populator.dto.StoreDataDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopulatorServiceImpl implements PopulatorService {

	private final ProviderConfig providerConfig;
	private final RestTemplate restTemplate;
	
	
	@Override
	public Optional<Integer> putCurrentPercentage(StoreDataDto storeDataDto) {
		log.trace("putCurrentPercentage method got storeDataDto: {}", storeDataDto);
		ResponseEntity<?> responseEntity = restTemplate.exchange(RequestEntity.put(getFullUrl(storeDataDto)).body(null),  new ParameterizedTypeReference<Optional<Integer>>(){});
		Optional<Integer>  result;
		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			result = (Optional<Integer>) responseEntity.getBody();
			log.trace("putCurrentPercentage method successful sent data to provider: {}", storeDataDto);
			result.ifPresentOrElse(
					(res) -> {
						log.trace("Putting current percentage ended with result: {}", res);		
					},
					() -> {
						log.error("Putting current percentage got nul from Provider");		
					}
				);
		} else {
			result = Optional.ofNullable(null);
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
