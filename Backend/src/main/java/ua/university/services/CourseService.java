package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.DAO.CourseDAO;
import ua.university.DAO.TeacherDAO;
import ua.university.models.Course;

import java.sql.SQLException;

public class CourseService {
    private CourseDAO courseDAO;

    public CourseService() throws SQLException, ClassNotFoundException {
        this.courseDAO = new CourseDAO();
    }

    public String indexCourse() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.courseDAO.indexCourse());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String addCourse(Course course) {
        this.courseDAO.saveCourse(course);
        ObjectMapper mapper = new ObjectMapper();
        try {
            course = this.courseDAO.getCourse(course.getName());
            return mapper.writeValueAsString(course);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCourse(long id){
        this.courseDAO.deleteCourse(id);
    }
}
