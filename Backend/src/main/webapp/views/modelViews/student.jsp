<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<% if (request.getAttribute("objectName") == "Student" && rolesSet != null && rolesSet.contains("admin")) { %>

    <% if (request.getAttribute("delete_id") == null) { %>
        <form action="/students" method="post">
            <div class="form-group">
                <label for="nameField">Name</label>
                <input type="text" name="name" class="form-control" id="nameField" placeholder="Enter name" required>
            </div>
            <input type="submit" name="btn_action" class="btn btn-primary" value="Add"/>
        </form>
    <% } %>


    <% if (request.getAttribute("delete_id") != null) { %>
        <form action="/students" method="post">
            <% session.setAttribute("delete_id", request.getAttribute("delete_id")); %>
            <input type="submit" name="btn_action" class="btn btn-danger" value="Delete"/>
        </form>
    <% } %>
<% } %>