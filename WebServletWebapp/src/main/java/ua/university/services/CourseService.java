package ua.university.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ua.university.DAO.CourseDAO;
import ua.university.models.Course;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class CourseService {
    private CourseDAO courseDAO;

    public CourseService() throws SQLException, ClassNotFoundException {
        this.courseDAO = new CourseDAO();
    }

    private static String objectToJson(Course data) {
        try {
            return new JSONObject(data).toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
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

            return array.toString();
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String indexCourse() throws SQLException {
        try {
            return objectsToJson(this.courseDAO.indexCourse());
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String getCourse(int id) throws SQLException {
        try {
            return objectToJson(this.courseDAO.getCourse(id));
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String addCourse(Course course) throws SQLException {
        try {
            int id = this.courseDAO.saveCourse(course);
            course.setId(id);
            return objectToJson(course);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (JSONException ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }

    public String updateCourse(int id, Course course) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.courseDAO.updateCourse(id, course);
            return mapper.writeValueAsString(course);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new JSONException(ex.getMessage());
        }
    }


    public void deleteCourse(int id) throws SQLException {
        try {
            this.courseDAO.deleteCourse(id);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            throw new SQLException(ex.getMessage());
        }
    }
}
