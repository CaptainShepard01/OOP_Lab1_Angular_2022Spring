package ua.university.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Course;
import ua.university.repositories.CourseRepository;
import ua.university.utils.ServerUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private static final String BAD_COURSE = "Bad course id parameter!";

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return this.courseRepository.findAll();
    }

    public Course getCourse(String id) {
        int intId = ServerUtils.parseParameterId(id);
        Optional<Course> data = courseRepository.findById(intId);
        if (data.isPresent()) {
            return data.get();
        } else {
            throw new BadRequestException(BAD_COURSE);
        }
    }

    @Transactional
    public Course updateCourse(String id, Course updatedCourse) {
        int intId = ServerUtils.parseParameterId(id);
        if (courseRepository.findById(intId).isPresent()) {
            return courseRepository.findById(intId)
                    .map(newCourse -> {
                        newCourse.setMaxGrade(updatedCourse.getMaxGrade());
                        newCourse.setName(updatedCourse.getName());
                        newCourse.setTeacher(updatedCourse.getTeacher());
                        newCourse.setMaxGrade(updatedCourse.getMaxGrade());
                        return courseRepository.save(newCourse);
                    })
                    .orElseGet(() -> {
                        updatedCourse.setId(intId);
                        return courseRepository.save(updatedCourse);
                    });
        } else {
            throw new BadRequestException(BAD_COURSE);
        }
    }

    @Transactional
    public void deleteCourse(String id) {
        int intId = ServerUtils.parseParameterId(id);
        if (courseRepository.findById(intId).isPresent()) {
            courseRepository.deleteById(intId);
        } else {
            throw new BadRequestException(BAD_COURSE);
        }
    }
}
