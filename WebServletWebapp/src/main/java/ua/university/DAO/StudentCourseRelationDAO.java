package ua.university.DAO;

import lombok.extern.slf4j.Slf4j;
import ua.university.config.DataSourceConfig;
import ua.university.models.StudentCourseRelation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StudentCourseRelationDAO {
    private final Connection connection;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public StudentCourseRelationDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    public List<StudentCourseRelation> indexStudentCourseRelation() throws SQLException {
        List<StudentCourseRelation> studentCourseRelationsList = new ArrayList<>();

        String sql = "SELECT * FROM student_course_relations";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int studentId = resultSet.getInt("student_id");
                int courseId = resultSet.getInt("course_id");
                int grade = resultSet.getInt("grade");
                String review = resultSet.getString("review");

                studentCourseRelationsList.add(new StudentCourseRelation(id,
                        this.studentDAO.getStudent(studentId),
                        this.courseDAO.getCourse(courseId),
                        grade,
                        review));
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }

        return studentCourseRelationsList;
    }

    public StudentCourseRelation getStudentCourseRelation(int id) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student_course_relations " +
                    "WHERE id=?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new StudentCourseRelation(resultSet.getInt("id"),
                        this.studentDAO.getStudent(resultSet.getInt("student_id")),
                        this.courseDAO.getCourse(resultSet.getInt("course_id")),
                        resultSet.getInt("grade"),
                        resultSet.getString("review"));
            }

            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public int getMaxGlobalId() throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM student_course_relations");

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

    public void saveStudentCourseRelation(StudentCourseRelation studentCourseRelation) throws SQLException {
        String sql1 = "SELECT id from students WHERE id=?";
        String sql2 = "SELECT id from courses WHERE id=?";
        String sql3 = "INSERT INTO student_course_relations (student_id, course_id, grade, review) VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2);
             PreparedStatement ps3 = connection.prepareStatement(sql3)) {
            connection.setAutoCommit(false);

            int studentId = studentCourseRelation.getStudent().getId();
            ps1.setInt(1, studentId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps3.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a student with id: %d", studentId);
                log.error(error);
                throw new SQLException(error);
            }

            int courseId = studentCourseRelation.getCourse().getId();
            ps2.setInt(1, courseId);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                ps3.setInt(2, rs2.getInt("id"));
            } else {
                String error = String.format("Could not find a course with id: %d", courseId);
                log.error(error);
                throw new SQLException(error);
            }

            ps3.setInt(3, studentCourseRelation.getGrade());
            ps3.setString(4, studentCourseRelation.getReview());
            ps3.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void updateStudentCourseRelation(int id, StudentCourseRelation updatedStudentCourseRelation) throws SQLException {
        String sql1 = "SELECT id from students WHERE id=?";
        String sql2 = "SELECT id from courses WHERE id=?";
        String sql3 = "SELECT id from student_course_relations WHERE id=?";
        String sql4 = "UPDATE student_course_relations SET student_id=?, course_id=?, grade=?, review=? WHERE id=?";


        try (PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2);
             PreparedStatement ps3 = connection.prepareStatement(sql3);
             PreparedStatement ps4 = connection.prepareStatement(sql4)) {
            connection.setAutoCommit(false);

            int studentId = updatedStudentCourseRelation.getStudent().getId();
            ps1.setInt(1, studentId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps4.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a student with id: %d", studentId);
                log.error(error);
                throw new SQLException(error);
            }

            int courseId = updatedStudentCourseRelation.getCourse().getId();
            ps2.setInt(1, courseId);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                ps4.setInt(2, rs2.getInt("id"));
            } else {
                String error = String.format("Could not find a course with id: %d", courseId);
                log.error(error);
                throw new SQLException(error);
            }

            ps3.setInt(1, id);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) {
                ps4.setInt(5, id);
            } else {
                String error = String.format("Could not find a s-c-r with id: %d to update", id);
                log.error(error);
                throw new SQLException(error);
            }

            ps4.setInt(3, updatedStudentCourseRelation.getGrade());
            ps4.setString(4, updatedStudentCourseRelation.getReview());

            ps4.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteStudentCourseRelation(int id) throws SQLException {
        String sql1 = "SELECT id from student_course_relations WHERE id=?";
        String sql2 = "DELETE FROM student_course_relations WHERE id = ?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
        PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, id);
            } else {
                String error = String.format("Could not find a s-c-r with id: %d to delete", id);
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

    public List<StudentCourseRelation> getStudentCourseRelationsForStudent(String studentName) throws SQLException {
        List<StudentCourseRelation> resultList = new ArrayList<>();

        String sql1 = "SELECT id FROM students WHERE name=?";
        String sql2 = "SELECT * FROM student_course_relations WHERE student_id=?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            ps1.setString(1, studentName);

            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a student with name: %s", studentName);
                log.error(error);
                throw new SQLException(error);
            }

            ResultSet resultSet = ps2.executeQuery();

            while (resultSet.next()) {
                resultList.add(new StudentCourseRelation(resultSet.getInt("id"),
                        this.studentDAO.getStudent(resultSet.getInt("student_id")),
                        this.courseDAO.getCourse(resultSet.getInt("course_id")),
                        resultSet.getInt("grade"),
                        resultSet.getString("review")));
            }

            return resultList;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public List<StudentCourseRelation> getStudentCourseRelationsForTeacher(String teacherName) throws SQLException {
        List<StudentCourseRelation> resultList = new ArrayList<>();

        String sql1 = "SELECT id FROM teachers WHERE name=?";
        String sql2 = "SELECT * FROM student_course_relations WHERE student_id=?";
        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            ps1.setString(1, teacherName);

            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, rs1.getInt("id"));
            } else {
                String error = String.format("Could not find a teacher with name: %s", teacherName);
                log.error(error);
                throw new SQLException(error);
            }

            ResultSet resultSet = ps2.executeQuery();

            while (resultSet.next()) {
                resultList.add(new StudentCourseRelation(resultSet.getInt("id"),
                        this.studentDAO.getStudent(resultSet.getInt("student_id")),
                        this.courseDAO.getCourse(resultSet.getInt("course_id")),
                        resultSet.getInt("grade"),
                        resultSet.getString("review")));
            }

            return resultList;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }
}
