package ua.university.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.university.modelEntities.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
