package cloud.apps;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

	@Bean
	public Mapper mapper() {
		return new DozerBeanMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}
