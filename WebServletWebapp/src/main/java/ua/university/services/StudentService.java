package ua.university.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import ua.university.DAO.StudentDAO;
import ua.university.models.Student;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.SQLException;

@Slf4j
public class StudentService {
    private StudentDAO studentDAO;

    public StudentService() throws SQLException, ClassNotFoundException {
        this.studentDAO = new StudentDAO();
    }

    private static String objectToJson(Student data) {
        try {
            return new JSONObject(data).toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    private static String objectsToJson(List<Student> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //Set pretty printing of json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            JSONArray array = new JSONArray();
            for (Student datum : data) {
                array.put(new JSONObject(datum));
            }

            return array.toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexStudent() throws SQLException {
        try {
            return objectsToJson(this.studentDAO.indexStudent());
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String getStudent(int id) throws SQLException {
        try {
            return objectToJson(this.studentDAO.getStudent(id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String addStudent(Student student) throws SQLException {
        try {
            int id = this.studentDAO.saveStudent(student);
            student.setId(id);
            return objectToJson(student);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String updateStudent(int id, Student student) throws SQLException {
        try {
            this.studentDAO.updateStudent(id, student);
            return objectToJson(student);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }


    public void deleteStudent(int id) throws SQLException {
        try {
            this.studentDAO.deleteStudent(id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }

    public String getStudentByName(String name) throws SQLException {
        try {
            return objectToJson(this.studentDAO.getStudent(name).get(0));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }
}
