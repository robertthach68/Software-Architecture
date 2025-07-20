package customers.repository;

import customers.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    // Get all students with a certain name
    List<Student> findByName(String name);

    // Get a student with a certain phoneNumber
    Student findByPhoneNumber(String phoneNumber);

    // Get all students from a certain city
    @Query("{'address.city' : ?0}")
    List<Student> findByCity(String city);
}