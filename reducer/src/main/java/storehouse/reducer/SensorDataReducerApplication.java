package storehouse.reducer;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import jakarta.annotation.PostConstruct;
import storehouse.reducer.dto.StoreDataDto;

@SpringBootApplication
public class SensorDataReducerApplication {


	public static void main(String[] args) {
		SpringApplication.run(SensorDataReducerApplication.class, args);
	}
	
}
