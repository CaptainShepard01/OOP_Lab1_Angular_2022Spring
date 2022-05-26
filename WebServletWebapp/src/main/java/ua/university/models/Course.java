package ua.university.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class Course implements IModel {
    private int id;
    private String name;
    private int maxGrade;

    private Teacher teacher;

    public Course() {
    }

    public Course(int id, String name, int maxGrade, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.maxGrade = maxGrade;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    @Override
    public String modelURLPattern() {
        return "/courses";
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(int maxGrade) {
        this.maxGrade = maxGrade;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Name: " +
                this.name +
                ", Max grade: " +
                this.maxGrade +
                ", Teacher: " +
                this.teacher;
    }

    @Override
    public Map<String, String> fieldsMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Id", String.valueOf(this.id));
        map.put("Name", this.name);
        map.put("Max grade", String.valueOf(this.maxGrade));
        map.put("Teacher", this.teacher.getName());
        return map;
    }
}
