package storehouse.reducer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;



@Configuration
public class Config {

	
	@Bean
	Gson configGson() {
		return new Gson();
	}
}
