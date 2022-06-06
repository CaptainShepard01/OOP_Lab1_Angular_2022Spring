package ua.university.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessToken;
import ua.university.models.StudentCourseRelation;
import ua.university.services.StudentCourseRelationService;
import ua.university.utils.KeycloakTokenUtil;
import ua.university.utils.ServletUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/studentCourseRelations/*")
@Slf4j
public class StudentCourseRelationController extends HttpServlet {
    private StudentCourseRelationService service;

    @Override
    public void init() {
        try {
            this.service = new StudentCourseRelationService();
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
            String data = "";

            AccessToken accessToken = KeycloakTokenUtil.getToken(request, request.getHeader("Authorization"));
            assert accessToken != null;
            String userName = "";
            String studentCourseRelationsJsonString = "";

            if (idValue == -1) {
                if (KeycloakTokenUtil.getRoles(accessToken).contains("ROLE_ADMIN")) {
                    studentCourseRelationsJsonString = this.service.indexStudentCourseRelation();
                } else if (KeycloakTokenUtil.getRoles(accessToken).contains("ROLE_STUDENT")) {
                    userName = KeycloakTokenUtil.getName(accessToken);
                    studentCourseRelationsJsonString = this.service.indexStudentCourseRelationForStudent(userName);
                } else if (KeycloakTokenUtil.getRoles(accessToken).contains("ROLE_TEACHER")) {
                    userName = KeycloakTokenUtil.getName(accessToken);
                    studentCourseRelationsJsonString = this.service.indexStudentCourseRelationForTeacher(userName);
                }

            } else {
                studentCourseRelationsJsonString = this.service.getStudentCourseRelation(idValue);
            }

            out.print(studentCourseRelationsJsonString);
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                response.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("S-c-r get error");
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

            StudentCourseRelation studentCourseRelation = new ObjectMapper().readValue(requestBody.toString(), StudentCourseRelation.class);

            if (studentCourseRelation.getGrade() > studentCourseRelation.getCourse().getMaxGrade()) {
                studentCourseRelation.setGrade(studentCourseRelation.getCourse().getMaxGrade());
            }

            String studentCourseRelationsJsonString = this.service.addStudentCourseRelation(studentCourseRelation);

            out.print(studentCourseRelationsJsonString);

        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("S-c-r post error");
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
            this.service.deleteStudentCourseRelation(id);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("S-c-r delete error");
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

            StudentCourseRelation studentCourseRelation = new ObjectMapper().readValue(requestBody.toString(), StudentCourseRelation.class);

            if (studentCourseRelation.getGrade() > studentCourseRelation.getCourse().getMaxGrade()) {
                studentCourseRelation.setGrade(studentCourseRelation.getCourse().getMaxGrade());
            }

            int idValue = ServletUtils.getURIId(req.getRequestURI());
            String studentCourseRelationJsonString = this.service.updateStudentCourseRelation(idValue, studentCourseRelation);

            out.print(studentCourseRelationJsonString);
            resp.setStatus(200);
        } catch (SQLException exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
            try {
                resp.getWriter().println(exception.getMessage());
            } catch (IOException e) {
                log.error("S-c-r update error");
            }
        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(exception.getMessage());
        }
    }
}