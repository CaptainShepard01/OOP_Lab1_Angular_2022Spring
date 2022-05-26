package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.DAO.StudentDAO;
import ua.university.models.Student;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.sql.SQLException;

public class StudentService {
    private StudentDAO studentDAO;

    public StudentService() throws SQLException, ClassNotFoundException {
        this.studentDAO = new StudentDAO();
    }

    private static String objectToJson(Student data) {
        try {
            return new JSONObject(data).toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
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
            JSONObject jo = new JSONObject();
            jo.put("students", array);
            JSONObject jo2 = new JSONObject();
            jo2.put("_embedded", jo);
            return jo2.toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String indexStudent() {
        try {
            return objectsToJson(this.studentDAO.indexStudent());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String getStudent(int id) {
        try {
            return objectToJson(this.studentDAO.getStudent(id));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String addStudent(Student student) {
        this.studentDAO.saveStudent(student);
        try {
            student.setId(this.studentDAO.getMaxGlobalId());
            return objectToJson(student);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String updateStudent(int id, Student student) {
        this.studentDAO.updateStudent(id, student);
        try {
            return objectToJson(student);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public void deleteStudent(int id) {
        try {
            this.studentDAO.deleteStudent(id);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String getStudentByName(String name) {
        try{
            return objectToJson(this.studentDAO.getStudent(name).get(0));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
        return "";
    }
}
