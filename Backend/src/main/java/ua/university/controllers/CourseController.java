package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.models.Course;
import ua.university.service.CourseControllerService;
import ua.university.utils.KeycloakTokenUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/courses")
public class CourseController extends HttpServlet {
    private CourseControllerService service;

    @Override
    public void init() {
        try {
            this.service = new CourseControllerService();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        super.service(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String coursesJsonString = this.service.indexCourses();

        out.print(coursesJsonString);
        out.flush();
//        request.setAttribute("username", KeycloakTokenUtil.getPreferredUsername(request));
//        request.setAttribute("roles", KeycloakTokenUtil.getRoles(request));

//        StringBuilder stringBuilder = new StringBuilder();
//
//        if (request.getParameter("id") == null) {
//            stringBuilder.append(service.showAll());
//        } else {
//            long course_id = Long.parseLong(request.getParameter("id"));
//            stringBuilder.append(service.showSingle(course_id));
//
//            request.setAttribute("delete_id", course_id);
//        }
//
//        request.setAttribute("teacherList", this.service.getAllTeachers());
//        request.setAttribute("objectName", "Course");
//
//        request.setAttribute("info", stringBuilder);
//        request.setAttribute("action", "None");
//
//        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/index.jsp");
//        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("POST request: "+req.getRequestURI());

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        StringBuilder requestBody = new StringBuilder();

        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Course course = new ObjectMapper().readValue(requestBody.toString(), Course.class);
        this.service.addCourse(course);

//        HttpSession session = req.getSession();
//        String action = req.getParameter("btn_action");
//        switch (action) {
//            case "Delete": {
//                long delete_id = Long.parseLong(session.getAttribute("delete_id").toString());
//                this.service.onDelete(delete_id);
//                resp.sendRedirect("/courses");
//                break;
//            }
//            case "Add": {
//                String name = req.getParameter("name");
//                String maxGrade = req.getParameter("maxGrade");
//                String teacherId = req.getParameter("teacher_id");
//                String[] params = new String[] {name, maxGrade, teacherId};
//                this.service.onAdd(params);
//                resp.sendRedirect("/courses");
//                break;
//            }
//            default: {
//                System.out.println("Not implemented action!");
//            }
//        }
//        session.invalidate();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("DELETE request: "+req.getRequestURI());

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}