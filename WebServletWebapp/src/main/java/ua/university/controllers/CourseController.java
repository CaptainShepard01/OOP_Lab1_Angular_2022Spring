package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.models.Course;
import ua.university.services.CourseService;
import ua.university.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/courses/*")
public class CourseController extends HttpServlet {
    private CourseService service;

    @Override
    public void init() {
        try {
            this.service = new CourseService();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        super.service(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            int idValue = ServletUtils.getURIId(request.getRequestURI());
            String data = "";

            String coursesJsonString = "";
            if (idValue == -1) {
                coursesJsonString = this.service.indexCourse();
            } else {
                coursesJsonString = this.service.getCourse(idValue);
            }

            out.print(coursesJsonString);
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder requestBody = new StringBuilder();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Course course = new ObjectMapper().readValue(requestBody.toString(), Course.class);
            String coursesJsonString = this.service.addCourse(course);

            out.print(coursesJsonString);

        }catch (Exception exception){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(exception);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = ServletUtils.getURIId(req.getRequestURI());
            this.service.deleteCourse(id);
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(exception);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder requestBody = new StringBuilder();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            int idValue = ServletUtils.getURIId(req.getRequestURI());
            Course course = new ObjectMapper().readValue(requestBody.toString(), Course.class);
            String courseJsonString = this.service.updateCourse(idValue, course);

            out.print(courseJsonString);
            resp.setStatus(200);
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(exception);
        }
    }
}