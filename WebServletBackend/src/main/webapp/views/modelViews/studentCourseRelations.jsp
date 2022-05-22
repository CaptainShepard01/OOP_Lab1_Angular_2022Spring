<%@ page import="java.util.List" %>
<%@ page import="ua.university.models.Student" %>
<%@ page import="ua.university.models.Course" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    if (request.getAttribute("objectName") == "StudentCourseRelation" && rolesSet != null &&
            (rolesSet.contains("student") ||
            rolesSet.contains("teacher") ||
            rolesSet.contains("admin"))) {
%>
    <% if (request.getAttribute("delete_id") == null && (rolesSet.contains("student") || rolesSet.contains("admin"))) { %>
        <form action="/studentCourseRelations" method="post">
            <div class="form-group">
                <label for="studentField">Select student</label>
                <select name="student_id" id="studentField" class="form-control" required>
                    <% for (Student student : (List<Student>) request.getAttribute("studentList")) { %>
                    <option value="<%=student.getId()%>"><%=student.getName().toString()%>
                    </option>
                    <% } %>
                </select>
            </div>
            <div class="form-group">
                <label for="courseField">Select course</label>
                <select name="course_id" id="courseField" class="form-control" required>
                    <% for (Course course : (List<Course>) request.getAttribute("courseList")) { %>
                    <option value="<%=course.getId()%>"><%=course.getName().toString() + ", Max grade: " + course.getMaxGrade()%>
                    </option>
                    <% } %>
                </select>
            </div>
            <% if (rolesSet.contains("admin")) { %>
                <div class="form-group">
                    <label for="gradeField">Grade</label>
                    <input type="number" name="grade" class="form-control" id="gradeField" placeholder="Enter grade" min="1"/>
                </div>
                <div class="form-group">
                    <label for="reviewField">Review</label>
                    <textarea type="text" rows="5" cols="40" name="review" class="form-control" id="reviewField" placeholder="Enter review"></textarea>
                </div>
            <% } %>
            <input type="submit" name="btn_action" class="btn btn-primary" value="Add"/>
        </form>
    <% } %>

    <% if (request.getAttribute("delete_id") != null && rolesSet.contains("admin")) { %>
    <form action="/studentCourseRelations" method="post">
        <% session.setAttribute("delete_id", request.getAttribute("delete_id")); %>
        <input type="submit" name="btn_action" class="btn btn-danger" value="Delete"/>
    </form>
    <% } %>

    <% if (request.getAttribute("delete_id") != null && rolesSet != null && (rolesSet.contains("admin") || rolesSet.contains("teacher"))) { %>
        <form action="/studentCourseRelations" method="post">
            <% session.setAttribute("update_id", request.getAttribute("delete_id")); %>
            <div class="form-group">
                <label for="gradeField">Grade</label>
                <input type="number" name="grade" class="form-control" id="gradeFieldUpdate" placeholder="Enter grade" required
                       min="1" value="<%= request.getAttribute("maxGrade") %>"/>
            </div>
            <div class="form-group">
                <label for="reviewField">Review</label>
                <textarea type="text" rows="5" cols="40" name="review" class="form-control" id="reviewFieldUpdate"
                          placeholder="Enter review" required><%= request.getAttribute("review") %></textarea>
            </div>
            <input type="submit" name="btn_action" class="btn btn-success" value="Update"/>
        </form>
    <% } %>
<% } %>