package customers;

import customers.domain.Address;
import customers.domain.Student;
import customers.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create addresses
		Address address1 = new Address("123 Main St", "New York", "10001");
		Address address2 = new Address("456 Oak Ave", "Los Angeles", "90210");
		Address address3 = new Address("789 Pine Rd", "Chicago", "60601");
		Address address4 = new Address("321 Elm St", "New York", "10002");
		Address address5 = new Address("654 Maple Dr", "Boston", "02101");

		// Create 5 students
		Student student1 = new Student("John Doe", "555-0101", "john.doe@email.com", address1);
		Student student2 = new Student("Jane Smith", "555-0102", "jane.smith@email.com", address2);
		Student student3 = new Student("Mike Johnson", "555-0103", "mike.johnson@email.com", address3);
		Student student4 = new Student("Sarah Wilson", "555-0104", "sarah.wilson@email.com", address4);
		Student student5 = new Student("David Brown", "555-0105", "david.brown@email.com", address5);

		// Save students to database
		studentRepository.save(student1);
		studentRepository.save(student2);
		studentRepository.save(student3);
		studentRepository.save(student4);
		studentRepository.save(student5);

		System.out.println("=== All Students ===");
		List<Student> allStudents = studentRepository.findAll();
		for (Student student : allStudents) {
			System.out.println(student);
		}

		System.out.println("\n=== Students with name 'John Doe' ===");
		List<Student> studentsByName = studentRepository.findByName("John Doe");
		for (Student student : studentsByName) {
			System.out.println(student);
		}

		System.out.println("\n=== Student with phone number '555-0102' ===");
		Student studentByPhone = studentRepository.findByPhoneNumber("555-0102");
		System.out.println(studentByPhone);

		System.out.println("\n=== Students from New York ===");
		List<Student> studentsFromCity = studentRepository.findByCity("New York");
		for (Student student : studentsFromCity) {
			System.out.println(student);
		}
	}
}
