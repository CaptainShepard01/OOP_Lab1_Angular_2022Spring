package ua.university.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.DAO.FacultyDAO;
import ua.university.models.Course;
import ua.university.models.Student;
import ua.university.models.StudentCourseRelation;
import ua.university.models.Teacher;
import ua.university.utils.Utils;

import java.sql.SQLException;
import java.util.List;

public class CourseControllerService{
    private FacultyDAO facultyDAO;

    public CourseControllerService() throws SQLException, ClassNotFoundException {
        this.facultyDAO = new FacultyDAO();
    }

    public String indexCourses(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this.facultyDAO.indexCourse());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public StringBuilder showAll() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<h2>Welcome to Courses table</h2>\n");

        Course[] courses = this.facultyDAO.indexCourse().toArray(new Course[0]);
        stringBuilder.append(Utils.getTable(courses));

        return stringBuilder;
    }

    public StringBuilder showSingle(long id) {
        StringBuilder stringBuilder = new StringBuilder();

        Course course = this.facultyDAO.getCourse(id);

        stringBuilder.append(Utils.getSingleModelView(course));

        return stringBuilder;
    }

    public void onAdd(String[] params) {
        String name = params[0];
        int maxGrade = Integer.parseInt(params[1]);
        Teacher teacher = this.facultyDAO.getTeacher(Long.parseLong(params[2]));
        this.facultyDAO.saveCourse(new Course(-1, name, maxGrade, teacher));
    }

    public void onDelete(long id) {
        this.facultyDAO.deleteCourse(id);
    }


    public List<Teacher> getAllTeachers() {
        return this.facultyDAO.indexTeacher();
    }

    public void addCourse(Course course) {
        this.facultyDAO.saveCourse(course);
    }
}
