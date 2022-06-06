package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            int idValue = ServletUtils.getURIId(request.getRequestURI());

            String coursesJsonString = "";
            if (idValue == -1) {
                coursesJsonString = this.service.indexCourse();
            } else {
                coursesJsonString = this.service.getCourse(idValue);
            }

            out.print(coursesJsonString);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Course get error");
            }
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            StringBuilder requestBody = new StringBuilder();
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }


            Course course = new ObjectMapper().readValue(requestBody.toString(), Course.class);
            String coursesJsonString = this.service.addCourse(course);

            out.print(coursesJsonString);

        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Course post error");
            }
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = ServletUtils.getURIId(request.getRequestURI());
            this.service.deleteCourse(id);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Course delete error");
            }
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try {
            StringBuilder requestBody = new StringBuilder();
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }


            int idValue = ServletUtils.getURIId(request.getRequestURI());
            Course course = new ObjectMapper().readValue(requestBody.toString(), Course.class);
            String courseJsonString = this.service.updateCourse(idValue, course);

            out.print(courseJsonString);
            response.setStatus(200);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("Course update error");
            }
        } catch (Exception exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }
}