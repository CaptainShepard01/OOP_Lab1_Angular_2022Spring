package ua.university.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONObject;
import ua.university.DAO.CourseDAO;
import ua.university.models.Course;

import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private CourseDAO courseDAO;

    public CourseService() throws SQLException, ClassNotFoundException {
        this.courseDAO = new CourseDAO();
    }

    private static String objectToJson(Course data) {
        try {
            return new JSONObject(data).toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    private static String objectsToJson(List<Course> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //Set pretty printing of json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            JSONArray array = new JSONArray();
            for (Course datum : data) {
                array.put(new JSONObject(datum));
            }
            JSONObject jo = new JSONObject();
            jo.put("courses", array);
            JSONObject jo2 = new JSONObject();
            jo2.put("_embedded", jo);
            return jo2.toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String indexCourse() {
        try {
            return objectsToJson(this.courseDAO.indexCourse());
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String getCourse(int id) {
        try {
            return objectToJson(this.courseDAO.getCourse(id));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String addCourse(Course course) {
        this.courseDAO.saveCourse(course);
        try {
            course.setId(this.courseDAO.getMaxGlobalId());
            return objectToJson(course);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "";
    }

    public String updateCourse(int id, Course course) {
        this.courseDAO.updateCourse(id, course);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(course);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteCourse(int id) {
        try {
            this.courseDAO.deleteCourse(id);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
