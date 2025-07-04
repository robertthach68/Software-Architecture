package contacts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import books.Book;

@SpringBootApplication
public class RestClientApplication implements CommandLineRunner {
	@Autowired
	private RestOperations restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(RestClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String serverUrl = "http://localhost:8080/api/books";

		// add Book 1
		restTemplate.postForLocation(serverUrl,
				new Book("978-0-7475-3269-9", "J.K. Rowling", "Harry Potter and the Philosopher's Stone", 29.99));
		// add Book 2
		restTemplate.postForLocation(serverUrl,
				new Book("978-0-7475-3849-3", "J.K. Rowling", "Harry Potter and the Chamber of Secrets", 32.99));

		// get book by ISBN
		Book book = restTemplate.getForObject(serverUrl + "/{isbn}", Book.class, "978-0-7475-3269-9");
		System.out.println("----------- get Book by ISBN-----------------------");
		System.out.println(book.getTitle() + " by " + book.getAuthor());

		// get all books
		Book[] books = restTemplate.getForObject(serverUrl, Book[].class);
		System.out.println("----------- get all books-----------------------");
		for (Book b : books) {
			System.out.println(b);
		}

		// delete book by ISBN
		restTemplate.delete(serverUrl + "/{isbn}", "978-0-7475-3849-3");

		// update book
		book.setPrice(34.99);
		restTemplate.put(serverUrl, book);

		// get all books after updates
		books = restTemplate.getForObject(serverUrl, Book[].class);
		System.out.println("----------- get all books after updates-----------------------");
		for (Book b : books) {
			System.out.println(b);
		}
	}

	@Bean
	RestOperations restTemplate() {
		return new RestTemplate();
	}
}
