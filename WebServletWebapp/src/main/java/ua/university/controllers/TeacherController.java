package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ua.university.models.Teacher;
import ua.university.services.TeacherService;
import ua.university.utils.ServletUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/teachers/*")
@Slf4j
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            int idValue = ServletUtils.getURIId(request.getRequestURI());

            String teachersJsonString = "";
            if (idValue == -1) {
                teachersJsonString = this.service.indexTeacher();
            } else {
                teachersJsonString = this.service.getTeacher(idValue);
            }

            out.print(teachersJsonString);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Teacher get error");
            }
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder requestBody = new StringBuilder();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            Teacher teacher = new ObjectMapper().readValue(requestBody.toString(), Teacher.class);
            String teachersJsonString = this.service.addTeacher(teacher);

            out.print(teachersJsonString);

        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Teacher post error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = ServletUtils.getURIId(req.getRequestURI());
            this.service.deleteTeacher(id);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Teacher delete error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            StringBuilder requestBody = new StringBuilder();
            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }


            int idValue = ServletUtils.getURIId(req.getRequestURI());
            Teacher teacher = new ObjectMapper().readValue(requestBody.toString(), Teacher.class);
            String teacherJsonString = this.service.updateTeacher(idValue, teacher);

            out.print(teacherJsonString);
            resp.setStatus(200);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Teacher update error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }
}