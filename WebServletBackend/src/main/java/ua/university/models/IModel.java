package ua.university.models;

import java.util.Map;

public interface IModel {
    Map<String, String> fieldsMap();
    int getId();
    String modelURLPattern();
}
