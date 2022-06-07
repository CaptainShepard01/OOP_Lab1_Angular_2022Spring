package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import ua.university.DAO.TeacherDAO;
import ua.university.models.Teacher;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.SQLException;

@Slf4j
public class TeacherService {
    private TeacherDAO teacherDAO;

    public TeacherService() throws SQLException, ClassNotFoundException {
        this.teacherDAO = new TeacherDAO();
    }

    private static String objectToJson(Teacher data) {
        try {
            return new JSONObject(data).toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    private static String objectsToJson(List<Teacher> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //Set pretty printing of json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            JSONArray array = new JSONArray();
            for (Teacher datum : data) {
                array.put(new JSONObject(datum));
            }

            return array.toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexTeacher() {
        try {
            return objectsToJson(this.teacherDAO.indexTeacher());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String getTeacher(int id) throws SQLException {
        try {
            return objectToJson(this.teacherDAO.getTeacher(id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String addTeacher(Teacher teacher) throws SQLException {
        try {
            int id = this.teacherDAO.saveTeacher(teacher);
            teacher.setId(id);
            return objectToJson(teacher);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String updateTeacher(int id, Teacher teacher) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.teacherDAO.updateTeacher(id, teacher);
            return mapper.writeValueAsString(teacher);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }


    public void deleteTeacher(int id) throws SQLException {
        try {
            this.teacherDAO.deleteTeacher(id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }
}
