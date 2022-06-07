package ua.university.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import ua.university.modelEntities.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByNameContaining(@RequestParam("name") String name);
}