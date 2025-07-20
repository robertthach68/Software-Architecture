package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Get all students with a certain name
    List<Student> findByName(String name);

    // Get a student with a certain phoneNumber
    Student findByPhoneNumber(String phoneNumber);

    // Get all students from a certain city
    @Query("SELECT s FROM Student s WHERE s.address.city = :city")
    List<Student> findByCity(@Param("city") String city);
}