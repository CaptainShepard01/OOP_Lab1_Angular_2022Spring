package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.DAO.TeacherDAO;
import ua.university.models.Teacher;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.SQLException;

public class TeacherService {
    private TeacherDAO teacherDAO;

    public TeacherService() throws SQLException, ClassNotFoundException {
        this.teacherDAO = new TeacherDAO();
    }

    private static String objectToJson(Teacher data) {
        try {
            return new JSONObject(data).toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
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
            JSONObject jo = new JSONObject();
            jo.put("teachers", array);
            JSONObject jo2 = new JSONObject();
            jo2.put("_embedded", jo);
            return jo2.toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String indexTeacher() {
        try {
            return objectsToJson(this.teacherDAO.indexTeacher());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String getTeacher(long id) {
        try {
            return objectToJson(this.teacherDAO.getTeacher(id));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String addTeacher(Teacher teacher) {
        this.teacherDAO.saveTeacher(teacher);
        try {
            teacher.setId(this.teacherDAO.getMaxGlobalId());
            return objectToJson(teacher);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String updateTeacher(int id, Teacher teacher) {
        this.teacherDAO.updateTeacher(id, teacher);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(teacher);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteTeacher(long id) {
        try {
            this.teacherDAO.deleteTeacher(id);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
