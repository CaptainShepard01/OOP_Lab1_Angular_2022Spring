package ua.university.service;

import ua.university.DAO.FacultyDAO;
import ua.university.models.Course;
import ua.university.models.Student;
import ua.university.models.StudentCourseRelation;
import ua.university.models.Teacher;
import ua.university.utils.Utils;

import java.sql.SQLException;
import java.util.List;

public class StudentControllerService{
    private FacultyDAO facultyDAO;

    public StudentControllerService() throws SQLException, ClassNotFoundException {
        this.facultyDAO = new FacultyDAO();
    }

    public StringBuilder showAll() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<h2>Welcome to Students table</h2>\n");

        Student[] students = this.facultyDAO.indexStudent().toArray(new Student[0]);
        stringBuilder.append(Utils.getTable(students));

        return stringBuilder;
    }
    public StringBuilder showSingle(long id) {
        StringBuilder stringBuilder = new StringBuilder();

        Student student = this.facultyDAO.getStudent(id);

        stringBuilder.append(Utils.getSingleModelView(student));

        return stringBuilder;
    }

    public void onAdd(String[] params) {
        String name = params[0];
        this.facultyDAO.saveStudent(new Student(-1, name));
    }

    public void onDelete(long id) {
        this.facultyDAO.deleteStudent(id);
    }
}
