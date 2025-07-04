package contacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestClientApplication implements CommandLineRunner {
	@Autowired
	private RestOperations restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(RestClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String serverUrl = "http://localhost:8080/contacts";

		// add Frank
		restTemplate.postForLocation(serverUrl, new Contact("Frank","Browns", "fbrowns@acme.com",
				"0639332163"));
		// add John
		restTemplate.postForLocation(serverUrl, new Contact("John","Doe", "jdoe@acme.com",
				"6739127563"));
		// get frank
		Contact contact= restTemplate.getForObject(serverUrl+"/{firstName}", Contact.class, "Frank");
		System.out.println("----------- get John-----------------------");
		System.out.println(contact.getFirstName()+" "+contact.getLastName());
        // get all
		Contacts contacts= restTemplate.getForObject(serverUrl, Contacts.class);
		System.out.println("----------- get all contacts-----------------------");
		System.out.println(contacts);

		// delete John
		restTemplate.delete(serverUrl+"/{firstName}", "John");

		// update frank
		contact.setEmail("franky@gmail.com");
		restTemplate.put(serverUrl+"/{firstName}", contact, contact.getFirstName());

		// get all
		contacts= restTemplate.getForObject(serverUrl, Contacts.class);
		System.out.println("----------- get all contacts-----------------------");
		System.out.println(contacts);
	}


	@Bean
	RestOperations restTemplate() {
		return new RestTemplate();
	}
}
