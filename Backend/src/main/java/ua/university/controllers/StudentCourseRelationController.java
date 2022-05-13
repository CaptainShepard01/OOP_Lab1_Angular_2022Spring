package ua.university.controllers;

import org.keycloak.representations.AccessToken;
import ua.university.models.StudentCourseRelation;
import ua.university.service.StudentCourseRelationControllerService;
import ua.university.utils.KeycloakTokenUtil;
import ua.university.utils.Utils;
import ua.university.models.Course;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

@WebServlet("/studentCourseRelations")
public class StudentCourseRelationController extends HttpServlet {
    private StudentCourseRelationControllerService service;

    @Override
    public void init() {
        try {
            this.service = new StudentCourseRelationControllerService();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("username", KeycloakTokenUtil.getPreferredUsername(request));
        Set<String> roles = KeycloakTokenUtil.getRoles(request);
        AccessToken accessToken = KeycloakTokenUtil.getToken(request);
        request.setAttribute("roles", roles);

        StringBuilder stringBuilder = new StringBuilder();

        if (request.getParameter("id") == null) {
            request.setAttribute("delete_id", null);
            if (roles.contains("admin")) {
                stringBuilder.append(service.showAll());
            } else if (roles.contains("student")) {
                stringBuilder.append(service.showAll(accessToken.getName(), Utils.Role.STUDENT));
            } else if (roles.contains("teacher")) {
                stringBuilder.append(service.showAll(accessToken.getName(), Utils.Role.TEACHER));
            }
        } else {
            long student_course_id = Long.parseLong(request.getParameter("id"));
            stringBuilder.append(service.showSingle(student_course_id));
            StudentCourseRelation studentCourseRelation = service.getStudentCourseRelation(student_course_id);

            request.setAttribute("delete_id", student_course_id);
            request.setAttribute("maxGrade", studentCourseRelation.getGrade());
            request.setAttribute("review", studentCourseRelation.getReview());
        }

        if (roles.contains("admin")) {
            request.setAttribute("studentList", this.service.getAllStudents());
            request.setAttribute("courseList", this.service.getAllCourses());
        } else if (roles.contains("student")) {
            request.setAttribute("studentList", this.service.getStudent(accessToken.getName()));
            request.setAttribute("courseList", this.service.getAllCourses());
        } else if (roles.contains("teacher")) {
            request.setAttribute("studentList", this.service.getAllStudents());
            request.setAttribute("courseList", this.service.getCoursesByTeacher(accessToken.getName()));
        }


        request.setAttribute("objectName", "StudentCourseRelation");

        request.setAttribute("info", stringBuilder);
        request.setAttribute("action", "None");

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/index.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("btn_action");
        switch (action) {
            case "Delete": {
                long delete_id = Long.parseLong(session.getAttribute("delete_id").toString());
                this.service.onDelete(delete_id);
                resp.sendRedirect("/studentCourseRelations");
                break;
            }
            case "Add": {
                String studentId = req.getParameter("student_id");
                String courseId = req.getParameter("course_id");
                String grade = (null == req.getParameter("grade") || req.getParameter("grade").equals("")) ? "0" : req.getParameter("grade");

                Course course = service.getCourseById(Long.parseLong(courseId));
                if (Integer.parseInt(grade) > course.getMaxGrade())
                    grade = String.valueOf(course.getMaxGrade());

                String review = (null == req.getParameter("review")) ? "" : req.getParameter("review");
                String[] params = new String[]{studentId, courseId, grade, review};
                this.service.onAdd(params);
                resp.sendRedirect("/studentCourseRelations");
                break;
            }
            case "Update": {
                long update_id = Long.parseLong(session.getAttribute("update_id").toString());
                StudentCourseRelation studentCourseRelation = service.getStudentCourseRelation(update_id);
                String grade = (null == req.getParameter("grade") || req.getParameter("grade").equals("")) ? "0" : req.getParameter("grade");

                Course course = studentCourseRelation.getCourse();
                if (Integer.parseInt(grade) > course.getMaxGrade())
                    grade = String.valueOf(course.getMaxGrade());

                String review = (null == req.getParameter("review")) ? "" : req.getParameter("review");
                String[] params = new String[]{String.valueOf(studentCourseRelation.getId()),
                        String.valueOf(studentCourseRelation.getStudent().getId()),
                        String.valueOf(studentCourseRelation.getCourse().getId()),
                        grade, review};
                this.service.onUpdate(params);
                resp.sendRedirect("/studentCourseRelations");
                break;
            }
            default: {
                System.out.println("Not implemented action!");
            }
        }
        session.invalidate();
    }
}