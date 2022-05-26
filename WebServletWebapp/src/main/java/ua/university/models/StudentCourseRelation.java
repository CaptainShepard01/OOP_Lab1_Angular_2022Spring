package ua.university.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StudentCourseRelation implements IModel {
    private int id;
    private Student student;
    private Course course;
    private int grade;
    private String review;

    public StudentCourseRelation() {
    }

    public StudentCourseRelation(int id, Student student, Course course, int grade, String review) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.grade = grade;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    @Override
    public String modelURLPattern() {
        return "/studentCourseRelations";
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "Student: " +
                this.course +
                ", Course: " +
                this.course +
                ", Grade: " +
                this.grade +
                ", Review: " +
                this.review;
    }

    @Override
    public Map<String, String> fieldsMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Id", String.valueOf(this.id));
        map.put("Student", this.student.getName());
        map.put("Course", this.course.getName()+", Max grade: " + this.course.getMaxGrade());
        map.put("Grade", String.valueOf(this.grade));
        map.put("Review", this.review);
        return map;
    }
}
