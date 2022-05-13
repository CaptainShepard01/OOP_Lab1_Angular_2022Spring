package ua.university.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Teacher implements IModel {
    private long id;
    private String name;

    public Teacher() {
    }

    public Teacher(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    @Override
    public String modelURLPattern() {
        return "/teachers";
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " +
                this.name;
    }

    @Override
    public Map<String, String> fieldsMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Id", String.valueOf(this.id));
        map.put("Name", this.name);
        return map;
    }
}
