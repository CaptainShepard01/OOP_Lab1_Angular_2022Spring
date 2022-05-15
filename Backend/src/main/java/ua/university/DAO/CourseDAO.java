package ua.university.DAO;

import ua.university.config.DataSourceConfig;
import ua.university.models.Course;
import ua.university.models.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Course> indexCourse() {
        List<Course> coursesList = new ArrayList<>();

        String sql = "SELECT * FROM courses";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int maxGrade = resultSet.getInt("max_grade");
                long teacherId = resultSet.getLong("teacher_id");
                Teacher teacher = this.teacherDAO.getTeacher(teacherId);

                coursesList.add(new Course(id, name, maxGrade, teacher));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println(" >>     " + e.getMessage());
        }

        return coursesList;
    }

    public Course getCourse(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses " +
                    "WHERE id=?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new Course(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("max_grade"),
                        this.teacherDAO.getTeacher(resultSet.getLong("teacher_id")));
            }

            return null;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return null;
        }
    }

    public Course getCourse(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses " +
                    "WHERE name=?");
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new Course(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("max_grade"),
                        this.teacherDAO.getTeacher(resultSet.getLong("teacher_id")));
            }

            return null;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return null;
        }
    }

    public List<Course> getCourses(String name) {
        List<Course> resultList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses " +
                    "WHERE teacher_id="+
                    "(SELECT id FROM teachers "+
                    "WHERE name=?)");
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultList.add(new Course(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("max_grade"),
                        this.teacherDAO.getTeacher(resultSet.getLong("teacher_id"))));
            }

            return resultList;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return resultList;
        }
    }

    public boolean saveCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO courses " +
                    "(name, max_grade, teacher_id) " +
                    "VALUES(?, ?, (SELECT id from teachers WHERE id=?))");
            statement.setString(1, course.getName());
            statement.setInt(2, course.getMaxGrade());
            statement.setLong(3, course.getTeacher().getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourse(long id, Course updatedCourse) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE courses " +
                            "SET name=?, max_grade=?, teacher_id=(SELECT id from teachers WHERE id=?)" +
                            "WHERE id=?"
            );
            statement.setString(1, updatedCourse.getName());
            statement.setInt(2, updatedCourse.getMaxGrade());
            statement.setLong(3, updatedCourse.getTeacher().getId());
            statement.setLong(4, id);
            int exec = statement.executeUpdate();

            if (exec > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM courses " +
                    "WHERE id = ?");
            statement.setLong(1, id);
            int exec = statement.executeUpdate();
            if (exec > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return false;
        }
    }
}
