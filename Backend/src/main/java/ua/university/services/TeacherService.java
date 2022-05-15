package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.DAO.TeacherDAO;
import ua.university.models.Teacher;

import java.sql.SQLException;

public class TeacherService {
    private TeacherDAO teacherDAO;

    public TeacherService() throws SQLException, ClassNotFoundException {
        this.teacherDAO = new TeacherDAO();
    }

    public String indexTeacher() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.teacherDAO.indexTeacher());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTeacher(Teacher teacher) {
        this.teacherDAO.saveTeacher(teacher);
    }
}
