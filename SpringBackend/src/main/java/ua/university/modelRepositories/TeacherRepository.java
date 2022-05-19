package ua.university.modelRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import ua.university.modelEntities.Teacher;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "teachers", path = "teachers")
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
