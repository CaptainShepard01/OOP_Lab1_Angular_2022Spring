package ua.university.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import ua.university.modelEntities.StudentCourseRelation;

import javax.transaction.Transactional;
import java.util.List;

public interface StudentCourseRelationRepository  extends JpaRepository<StudentCourseRelation, Integer> {

    @Query(value = "SELECT * FROM student_course_relations WHERE student_id=(SELECT id FROM students WHERE name=?)",
            nativeQuery = true)
    List<StudentCourseRelation> findByStudentNameContaining(@RequestParam("name") String name);

    @Query(value = "SELECT * FROM student_course_relations WHERE course_id=(SELECT id FROM courses WHERE teacher_id=(SELECT id FROM teachers WHERE name=?))",
            nativeQuery = true)
    List<StudentCourseRelation> findByTeacherNameContaining(@RequestParam("name") String name);
}