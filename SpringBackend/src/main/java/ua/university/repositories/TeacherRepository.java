package ua.university.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import ua.university.modelEntities.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
