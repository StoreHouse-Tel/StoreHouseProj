package storehouse.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class AccountProviderConfiguration {

	@Value("${app.account_provider.host}")
	String host;
	@Value("${app.account_provider.port}")
	int port;
	@Value("${app.account_provider.urn}")
	String url;
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}