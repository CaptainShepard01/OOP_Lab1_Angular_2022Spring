package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.models.Course;
import ua.university.models.Teacher;
import ua.university.services.TeacherService;
import ua.university.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/teachers/*")
public class TeacherController extends HttpServlet {
    private TeacherService service;

    @Override
    public void init() {
        try {
            this.service = new TeacherService();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            int idValue = ServletUtils.getURIId(request.getRequestURI());
            String data = "";

            String teachersJsonString = "";
            if (idValue == -1) {
                teachersJsonString = this.service.indexTeacher();
            } else {
                teachersJsonString = this.service.getTeacher(idValue);
            }

            out.print(teachersJsonString);
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

            Teacher teacher = new ObjectMapper().readValue(requestBody.toString(), Teacher.class);
            String teachersJsonString = this.service.addTeacher(teacher);

            out.print(teachersJsonString);

        }catch (Exception exception){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(exception);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = ServletUtils.getURIId(req.getRequestURI());
            this.service.deleteTeacher(id);
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(exception);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

            int id = ServletUtils.getURIId(req.getRequestURI());
            Teacher teacher = new ObjectMapper().readValue(requestBody.toString(), Teacher.class);
            String teacherJsonString = this.service.updateTeacher(id, teacher);

            out.print(teacherJsonString);
            resp.setStatus(200);
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println(exception);
        }
    }
}