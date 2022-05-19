package ua.university.modelRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import ua.university.modelEntities.Course;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "courses", path = "courses")
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
