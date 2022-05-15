package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.DAO.StudentCourseRelationDAO;
import ua.university.models.*;

import java.sql.SQLException;

public class StudentCourseRelationService {
    private StudentCourseRelationDAO studentCourseRelationDAO;

    public StudentCourseRelationService() throws SQLException, ClassNotFoundException {
        this.studentCourseRelationDAO = new StudentCourseRelationDAO();
    }

    public String indexStudentCourseRelation() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.studentCourseRelationDAO.indexStudentCourseRelation());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTeacher(StudentCourseRelation studentCourseRelation) {
        this.studentCourseRelationDAO.saveStudentCourseRelation(studentCourseRelation);
    }
}
