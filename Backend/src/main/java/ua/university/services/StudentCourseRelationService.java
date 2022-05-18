package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONObject;
import ua.university.DAO.StudentCourseRelationDAO;
import ua.university.models.*;

import java.sql.SQLException;
import java.util.List;

public class StudentCourseRelationService {
    private StudentCourseRelationDAO studentCourseRelationDAO;

    public StudentCourseRelationService() throws SQLException, ClassNotFoundException {
        this.studentCourseRelationDAO = new StudentCourseRelationDAO();
    }

    public void addTeacher(StudentCourseRelation studentCourseRelation) {
        this.studentCourseRelationDAO.saveStudentCourseRelation(studentCourseRelation);
    }

    private static String objectToJson(StudentCourseRelation data) {
        try {
            return new JSONObject(data).toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
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
            JSONObject jo = new JSONObject();
            jo.put("studentCourseRelations", array);
            JSONObject jo2 = new JSONObject();
            jo2.put("_embedded", jo);
            return jo2.toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String indexStudentCourseRelation() {
        try {
            return objectsToJson(this.studentCourseRelationDAO.indexStudentCourseRelation());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String getStudentCourseRelation(long id) {
        try {
            return objectToJson(this.studentCourseRelationDAO.getStudentCourseRelation(id));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String addStudentCourseRelation(StudentCourseRelation studentCourseRelation) {
        this.studentCourseRelationDAO.saveStudentCourseRelation(studentCourseRelation);
        try {
            studentCourseRelation.setId(this.studentCourseRelationDAO.getMaxGlobalId());
            return objectToJson(studentCourseRelation);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String updateStudentCourseRelation(int id, StudentCourseRelation studentCourseRelation) {
        this.studentCourseRelationDAO.updateStudentCourseRelation(id, studentCourseRelation);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(studentCourseRelation);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteStudentCourseRelation(long id) {
        try {
            this.studentCourseRelationDAO.deleteStudentCourseRelation(id);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
