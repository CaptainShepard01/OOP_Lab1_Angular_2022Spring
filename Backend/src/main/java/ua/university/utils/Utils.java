package ua.university.utils;

import ua.university.models.IModel;

import java.util.Map;

public class Utils {
    public static enum Role {
        STUDENT,
        TEACHER
    }
    public Utils() {
    }
    public static StringBuilder getSingleModelView(IModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> map = model.fieldsMap();

        stringBuilder.append("<table  class=\"w-auto table table-bordered table-sm\">\n<tbody>\n");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            stringBuilder.append("<tr>\n<td>").append(entry.getKey()).append("</td>\n")
                    .append("<td>").append(entry.getValue()).append("</td>\n</tr>\n");
        }

        stringBuilder.append("</tbody>\n</table>");

        return stringBuilder;
    }

    public static StringBuilder getTable(IModel[] models) {
        StringBuilder stringBuilder = new StringBuilder();

        if (models.length == 0)
            return stringBuilder;

        Map<String, String> map = models[0].fieldsMap();
        stringBuilder.append("<table  class=\"w-auto table models-table table-bordered table-hover table-sm\">\n<thead>\n<tr>\n");

        for (String key : map.keySet()) {
            stringBuilder.append("<th scope=\"col\">").append(key).append("</th>\n");
        }

        stringBuilder.append("</tr>\n</thead>\n<tbody>\n");
        for (IModel model : models) {
            stringBuilder.append(getRow(model));
        }
        stringBuilder.append("</tbody>\n</table>\n");

        return stringBuilder;
    }

    public static StringBuilder getRow(IModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> map = model.fieldsMap();

        stringBuilder.append("<tr onclick=\"window.location.href = '")
                .append(model.modelURLPattern()).append("?id=")
                .append(model.getId())
                .append("';\">\n");
        for (String value : map.values()) {
            stringBuilder.append("<td>").append(value).append("</td>\n");
        }
        stringBuilder.append("</tr>\n");

        return stringBuilder;
    }
}
