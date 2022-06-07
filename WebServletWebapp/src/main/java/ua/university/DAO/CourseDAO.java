package ua.university.DAO;

import lombok.extern.slf4j.Slf4j;
import ua.university.config.DataSourceConfig;
import ua.university.models.Course;
import ua.university.models.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CourseDAO {
    private final Connection connection;
    private final TeacherDAO teacherDAO;

    public CourseDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
        teacherDAO = new TeacherDAO();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    public List<Course> indexCourse() throws SQLException {
        List<Course> coursesList = new ArrayList<>();

        String sql = "SELECT * FROM courses";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int maxGrade = resultSet.getInt("max_grade");
                int teacherId = resultSet.getInt("teacher_id");
                Teacher teacher = this.teacherDAO.getTeacher(teacherId);

                coursesList.add(new Course(id, name, maxGrade, teacher));
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }

        return coursesList;
    }

    public Course getCourse(int id) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses " +
                    "WHERE id=?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Course(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("max_grade"),
                        this.teacherDAO.getTeacher(resultSet.getInt("teacher_id")));
            }

            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public Course getCourse(String name) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses " +
                    "WHERE name=?");
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Course(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("max_grade"),
                        this.teacherDAO.getTeacher(resultSet.getInt("teacher_id")));
            }

            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public int getMaxGlobalId() throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM courses");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("max");
            }

            return -1;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public List<Course> getCourses(String name) throws SQLException {
        List<Course> resultList = new ArrayList<>();

        String sql1 = "SELECT id FROM teachers WHERE name=?";
        String sql2 = "SELECT * FROM courses WHERE teacher_id=?";

        try (PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            ps1.setString(1, name);

            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                ps2.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a teacher with name: %s", name);
                log.error(error);
                throw new SQLException(error);
            }

            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                resultList.add(new Course(rs2.getInt("id"),
                        rs2.getString("name"),
                        rs2.getInt("max_grade"),
                        this.teacherDAO.getTeacher(rs2.getInt("teacher_id"))));
            }

            return resultList;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public int saveCourse(Course course) throws SQLException {
        String sql1 = "SELECT id from teachers WHERE id=?";
        String sql2 = "INSERT INTO courses (name, max_grade, teacher_id) VALUES(?, ?, ?) RETURNING id";

        try (PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);
            ps1.setInt(1, course.getTeacher().getId());

            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                ps2.setInt(3, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a teacher with id: %d", course.getTeacher().getId());
                log.error(error);
                throw new SQLException(error);
            }

            ps2.setString(1, course.getName());
            ps2.setInt(2, course.getMaxGrade());
            ps2.executeUpdate();

            ResultSet rs2 = ps2.executeQuery();
            connection.commit();

            if (rs2.next()) {
                return rs2.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void updateCourse(int id, Course updatedCourse) throws SQLException {
        String sql1 = "SELECT id from teachers WHERE id=?";
        String sql2 = "SELECT id from courses WHERE id=?";
        String sql3 = "UPDATE courses SET name=?, max_grade=?, teacher_id=? WHERE id=?";

        try (PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2);
             PreparedStatement ps3 = connection.prepareStatement(sql3)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, updatedCourse.getTeacher().getId());
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                ps3.setInt(3, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a teacher with id: %d", updatedCourse.getTeacher().getId());
                log.error(error);
                throw new SQLException(error);
            }

            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                ps3.setInt(4, id);
            } else {
                String error = String.format("Could not find a course with id: %d to update", id);
                log.error(error);
                throw new SQLException(error);
            }

            ps3.setString(1, updatedCourse.getName());
            ps3.setInt(2, updatedCourse.getMaxGrade());
            ps3.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteCourse(int id) throws SQLException {
        String sql1 = "SELECT id from courses WHERE id=?";
        String sql2 = "DELETE FROM courses WHERE id = ?";

        try (PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, id);
            } else {
                String error = String.format("Could not find a course with id: %d to delete", id);
                log.error(error);
                throw new SQLException(error);
            }

            ps2.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }
}
