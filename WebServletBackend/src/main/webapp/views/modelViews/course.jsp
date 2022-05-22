<%@ page import="ua.university.models.Teacher" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% if (request.getAttribute("objectName") == "Course" && rolesSet != null && rolesSet.contains("admin")) { %>

    <% if (request.getAttribute("delete_id") == null) { %>
        <form action="/courses" method="post">
            <div class="form-group">
                <label for="nameField">Name</label>
                <input type="text" name="name" class="form-control" id="nameField" placeholder="Enter name" required>
            </div>
            <div class="form-group">
                <label for="maxGradeField">Max grade</label>
                <input type="number" name="maxGrade" class="form-control" id="maxGradeField" placeholder="Enter max grade"
                       required>
            </div>
            <div class="form-group">
                <label for="teacherField">Teacher</label>
                <select name="teacher_id" id="teacherField" class="form-control" required>
                    <% for (Teacher teacher : (List<Teacher>) request.getAttribute("teacherList")) { %>
                    <option value="<%=teacher.getId()%>"><%=teacher.getName().toString()%>
                    </option>
                    <% } %>
                </select>
            </div>
            <input type="submit" name="btn_action" class="btn btn-primary" value="Add"/>
        </form>
    <% } %>


    <% if (request.getAttribute("delete_id") != null) { %>
        <form action="/courses" method="post">
            <% session.setAttribute("delete_id", request.getAttribute("delete_id")); %>
            <input type="submit" name="btn_action" class="btn btn-danger" value="Delete" />
        </form>
    <% } %>

<% } %>