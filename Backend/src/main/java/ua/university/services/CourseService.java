package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.DAO.CourseDAO;
import ua.university.DAO.TeacherDAO;
import ua.university.models.Course;

import java.sql.SQLException;

public class CourseService {
    private CourseDAO courseDAO;
    private TeacherDAO teacherDAO;

    public CourseService() throws SQLException, ClassNotFoundException {
        this.courseDAO = new CourseDAO();
        this.teacherDAO = new TeacherDAO();
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
            course.setTeacher(this.teacherDAO.getTeacher(course.getTeacher().getId()));
            return mapper.writeValueAsString(course);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCourse(long id){
        this.courseDAO.deleteCourse(id);
    }
}
