package ua.university.DAO;

import ua.university.config.DataSourceConfig;
import ua.university.models.Course;
import ua.university.models.Student;
import ua.university.models.StudentCourseRelation;
import ua.university.models.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacultyDAO {
    private final Connection connection;

    public FacultyDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    // -------------- Courses block --------------

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
                Teacher teacher = getTeacher(teacherId);

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
                        getTeacher(resultSet.getLong("teacher_id")));
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
                        getTeacher(resultSet.getLong("teacher_id"))));
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

    // -------------- Teachers block --------------

    public List<Teacher> indexTeacher() {
        List<Teacher> teachersList = new ArrayList<>();

        String sql = "SELECT * FROM teachers";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                teachersList.add(new Teacher(id, name));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println(" >>     " + e.getMessage());
        }

        return teachersList;
    }

    public Teacher getTeacher(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM teachers " +
                    "WHERE id=?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new Teacher(resultSet.getLong("id"),
                        resultSet.getString("name"));
            }

            return null;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return null;
        }
    }

    public boolean saveTeacher(Teacher teacher) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO teachers " +
                    "(name) " +
                    "VALUES(?)");
            statement.setString(1, teacher.getName());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return false;
        }
    }

    public boolean updateTeacher(long id, Teacher updatedTeacher) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE teachers " +
                            "SET name=?" +
                            "WHERE id=?"
            );
            statement.setString(1, updatedTeacher.getName());
            statement.setLong(2, id);
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

    public boolean deleteTeacher(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM teachers " +
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

    // -------------- Students block --------------

    public List<Student> indexStudent() {
        List<Student> studentsList = new ArrayList<>();

        String sql = "SELECT * FROM students";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                studentsList.add(new Student(id, name));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println(" >>     " + e.getMessage());
        }

        return studentsList;
    }

    public Student getStudent(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM students " +
                    "WHERE id=?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new Student(resultSet.getLong("id"),
                        resultSet.getString("name"));
            }

            return null;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return null;
        }
    }

    public List<Student> getStudent(String name) {
        List<Student> resultList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM students " +
                    "WHERE name=?");
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultList.add(new Student(resultSet.getLong("id"),
                        resultSet.getString("name")));
            }

            return resultList;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return resultList;
        }
    }

    public boolean saveStudent(Student student) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students " +
                    "(name) " +
                    "VALUES(?)");
            statement.setString(1, student.getName());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return false;
        }
    }

    public boolean updateStudent(long id, Student updatedStudent) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE students " +
                            "SET name=?" +
                            "WHERE id=?"
            );
            statement.setString(1, updatedStudent.getName());
            statement.setLong(2, id);
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

    public boolean deleteStudent(long id) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students " +
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

    // -------------- Student-Course relation block --------------

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
                        getStudent(studentId),
                        getCourse(courseId),
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
                        getStudent(resultSet.getLong("student_id")),
                        getCourse(resultSet.getLong("course_id")),
                        resultSet.getInt("grade"),
                        resultSet.getString("review"));
            }

            return null;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return null;
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
                        getStudent(resultSet.getLong("student_id")),
                        getCourse(resultSet.getLong("course_id")),
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
                        getStudent(resultSet.getLong("student_id")),
                        getCourse(resultSet.getLong("course_id")),
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
