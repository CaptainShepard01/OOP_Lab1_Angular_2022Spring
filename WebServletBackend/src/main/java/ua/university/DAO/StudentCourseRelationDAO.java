package ua.university.DAO;

import ua.university.config.DataSourceConfig;
import ua.university.models.StudentCourseRelation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<StudentCourseRelation> indexStudentCourseRelation() {
        List<StudentCourseRelation> studentCourseRelationsList = new ArrayList<>();

        String sql = "SELECT * FROM student_course_relations";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long studentId = resultSet.getLong("student_id");
                long courseId = resultSet.getLong("course_id");
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
            System.err.println(" >>     " + e.getMessage());
        }

        return studentCourseRelationsList;
    }

    public StudentCourseRelation getStudentCourseRelation(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student_course_relations " +
                    "WHERE id=?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new StudentCourseRelation(resultSet.getLong("id"),
                        this.studentDAO.getStudent(resultSet.getLong("student_id")),
                        this.courseDAO.getCourse(resultSet.getLong("course_id")),
                        resultSet.getInt("grade"),
                        resultSet.getString("review"));
            }

            return null;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return null;
        }
    }

    public int getMaxGlobalId() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM student_course_relations");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getInt("max");
            }

            return -1;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return -1;
        }
    }

    public boolean saveStudentCourseRelation(StudentCourseRelation studentCourseRelation) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO student_course_relations " +
                    "(student_id, course_id, grade, review) " +
                    "VALUES((SELECT id from students WHERE id=?), (SELECT id from courses WHERE id=?), ?, ?)");
            statement.setLong(1, studentCourseRelation.getStudent().getId());
            statement.setLong(2, studentCourseRelation.getCourse().getId());
            statement.setInt(3, studentCourseRelation.getGrade());
            statement.setString(4, studentCourseRelation.getReview());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return false;
        }
    }

    public boolean updateStudentCourseRelation(long id, StudentCourseRelation updatedStudentCourseRelation) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE student_course_relations " +
                            "SET student_id=(SELECT id from students WHERE id=?), course_id=(SELECT id from courses WHERE id=?), grade=?, review=?" +
                            "WHERE id=?"
            );
            statement.setLong(1, updatedStudentCourseRelation.getStudent().getId());
            statement.setLong(2, updatedStudentCourseRelation.getCourse().getId());
            statement.setInt(3, updatedStudentCourseRelation.getGrade());
            statement.setString(4, updatedStudentCourseRelation.getReview());
            statement.setLong(5, id);
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

    public boolean deleteStudentCourseRelation(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM student_course_relations " +
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

    public List<StudentCourseRelation> getStudentCourseRelationsForStudent(String studentName) {
        List<StudentCourseRelation> resultList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student_course_relations " +
                    "WHERE student_id=("+
                    "SELECT id FROM students WHERE name=?)");
            statement.setString(1, studentName);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultList.add(new StudentCourseRelation(resultSet.getLong("id"),
                        this.studentDAO.getStudent(resultSet.getLong("student_id")),
                        this.courseDAO.getCourse(resultSet.getLong("course_id")),
                        resultSet.getInt("grade"),
                        resultSet.getString("review")));
            }

            return resultList;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return resultList;
        }
    }

    public List<StudentCourseRelation> getStudentCourseRelationsForTeacher(String teacherName) {
        List<StudentCourseRelation> resultList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student_course_relations " +
                    "WHERE course_id=("+
                    "SELECT id FROM courses WHERE teacher_id=("+
                    "SELECT id FROM teachers WHERE name=?))");
            statement.setString(1, teacherName);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultList.add(new StudentCourseRelation(resultSet.getLong("id"),
                        this.studentDAO.getStudent(resultSet.getLong("student_id")),
                        this.courseDAO.getCourse(resultSet.getLong("course_id")),
                        resultSet.getInt("grade"),
                        resultSet.getString("review")));
            }

            return resultList;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return resultList;
        }
    }
}
