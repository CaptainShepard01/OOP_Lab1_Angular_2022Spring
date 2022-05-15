package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.DAO.StudentDAO;
import ua.university.models.Student;

import java.sql.SQLException;

public class StudentService {
    private StudentDAO studentDAO;

    public StudentService() throws SQLException, ClassNotFoundException {
        this.studentDAO = new StudentDAO();
    }

    public String indexStudent() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.studentDAO.indexStudent());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addStudent(Student student) {
        this.studentDAO.saveStudent(student);
    }
}
