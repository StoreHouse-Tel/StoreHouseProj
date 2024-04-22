package storehouse.populator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class ProviderConfig {

	@Value("${app.container_provider.host}")
	String host;
	@Value("${app.container_provider.port}")
	int port;
	@Value("${app.container_provider.put_current_percentage}")
	String urlTemplatePutNamePercentage;
		
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
