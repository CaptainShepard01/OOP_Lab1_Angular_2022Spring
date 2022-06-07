package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ua.university.DAO.StudentCourseRelationDAO;
import ua.university.models.*;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class StudentCourseRelationService {
    private StudentCourseRelationDAO studentCourseRelationDAO;

    public StudentCourseRelationService() throws SQLException, ClassNotFoundException {
        this.studentCourseRelationDAO = new StudentCourseRelationDAO();
    }

    private static String objectToJson(StudentCourseRelation data) {
        try {
            return new JSONObject(data).toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    private static String objectsToJson(List<StudentCourseRelation> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //Set pretty printing of json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            JSONArray array = new JSONArray();
            for (StudentCourseRelation datum : data) {
                array.put(new JSONObject(datum));
            }

            return array.toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexStudentCourseRelation() throws SQLException {
        try {
            return objectsToJson(this.studentCourseRelationDAO.indexStudentCourseRelation());
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexStudentCourseRelationForStudent(String studentName) throws SQLException {
        try {
            return objectsToJson(this.studentCourseRelationDAO.getStudentCourseRelationsForStudent(studentName));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexStudentCourseRelationForTeacher(String teacherName) throws SQLException {
        try {
            return objectsToJson(this.studentCourseRelationDAO.getStudentCourseRelationsForTeacher(teacherName));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String getStudentCourseRelation(int id) throws SQLException {
        try {
            return objectToJson(this.studentCourseRelationDAO.getStudentCourseRelation(id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String addStudentCourseRelation(StudentCourseRelation studentCourseRelation) throws SQLException {
        try {
            int id =this.studentCourseRelationDAO.saveStudentCourseRelation(studentCourseRelation);
            studentCourseRelation.setId(id);
            return objectToJson(studentCourseRelation);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String updateStudentCourseRelation(int id, StudentCourseRelation studentCourseRelation) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.studentCourseRelationDAO.updateStudentCourseRelation(id, studentCourseRelation);
            return mapper.writeValueAsString(studentCourseRelation);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }


    public void deleteStudentCourseRelation(int id) throws SQLException {
        try {
            this.studentCourseRelationDAO.deleteStudentCourseRelation(id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }
}
