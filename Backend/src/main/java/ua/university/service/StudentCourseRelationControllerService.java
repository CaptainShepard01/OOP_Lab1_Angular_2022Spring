package ua.university.service;

import ua.university.DAO.FacultyDAO;
import ua.university.models.*;
import ua.university.models.Teacher;
import ua.university.utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseRelationControllerService{
    private FacultyDAO facultyDAO;

    public StudentCourseRelationControllerService() throws SQLException, ClassNotFoundException {
        this.facultyDAO = new FacultyDAO();
    }

    public StringBuilder showAll() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<h2>Welcome to Student-Course relations table</h2>\n");

        StudentCourseRelation[] studentCourseRelations = this.facultyDAO.indexStudentCourseRelation().toArray(new StudentCourseRelation[0]);
        stringBuilder.append(Utils.getTable(studentCourseRelations));

        return stringBuilder;
    }

    public StringBuilder showAll(String name, Utils.Role role) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<h2>Welcome to Student-Course relations table</h2>\n");
        List<StudentCourseRelation> studentCourseRelations = new ArrayList<>();

        switch (role){
            case STUDENT: {
                studentCourseRelations = this.facultyDAO.getStudentCourseRelationsForStudent(name);
                break;
            }
            case TEACHER:{
                studentCourseRelations = this.facultyDAO.getStudentCourseRelationsForTeacher(name);
                break;
            }
        }

        StudentCourseRelation[] array = studentCourseRelations.toArray(new StudentCourseRelation[0]);

        stringBuilder.append(Utils.getTable(array));

        return stringBuilder;
    }

    public StringBuilder showSingle(long id) {
        StringBuilder stringBuilder = new StringBuilder();

        StudentCourseRelation studentCourseRelation = this.facultyDAO.getStudentCourseRelation(id);

        stringBuilder.append(Utils.getSingleModelView(studentCourseRelation));

        return stringBuilder;
    }

    public void onAdd(String[] params) {
        long studentId = Long.parseLong(params[0]);
        Student student = this.facultyDAO.getStudent(studentId);
        long courseId = Long.parseLong(params[1]);
        Course course = this.facultyDAO.getCourse(courseId);
        int grade = Integer.parseInt(params[2]);
        String review = params[3];
        this.facultyDAO.saveStudentCourseRelation(new StudentCourseRelation(-1, student, course, grade, review));
    }

    public void onDelete(long id) {
        this.facultyDAO.deleteStudentCourseRelation(id);
    }

    public void onUpdate(String[] params) {
        long id = Long.parseLong(params[0]);
        long studentId = Long.parseLong(params[1]);
        Student student = this.facultyDAO.getStudent(studentId);
        long courseId = Long.parseLong(params[2]);
        Course course = this.facultyDAO.getCourse(courseId);
        int grade = Integer.parseInt(params[3]);
        String review = params[4];
        this.facultyDAO.updateStudentCourseRelation(id, new StudentCourseRelation(-1, student, course, grade, review));
    }

    public List<Student> getAllStudents() {
        return this.facultyDAO.indexStudent();
    }

    public List<Course> getAllCourses() {
        return this.facultyDAO.indexCourse();
    }

    public StudentCourseRelation getStudentCourseRelation(long student_course_id) {
        return this.facultyDAO.getStudentCourseRelation(student_course_id);
    }

    public List<Student> getStudent(String name) {
        return this.facultyDAO.getStudent(name);
    }

    public List<Course> getCoursesByTeacher(String name) {
        return this.facultyDAO.getCourses(name);
    }

    public Course getCourseById(long id) {
        return this.facultyDAO.getCourse(id);
    }

}
