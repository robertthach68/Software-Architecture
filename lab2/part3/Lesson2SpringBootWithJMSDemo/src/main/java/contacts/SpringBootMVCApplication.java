package contacts;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class SpringBootMVCApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMVCApplication.class, args);
	}

}
