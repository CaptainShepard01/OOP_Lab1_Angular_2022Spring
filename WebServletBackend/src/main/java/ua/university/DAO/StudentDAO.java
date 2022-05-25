package ua.university.DAO;

import ua.university.config.DataSourceConfig;
import ua.university.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private final Connection connection;

    public StudentDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    public List<Student> indexStudent() {
        List<Student> studentsList = new ArrayList<>();

        String sql = "SELECT * FROM students";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                studentsList.add(new Student(id, name));
            }
            resultSet.close();
        } catch (SQLException e) {
            System.err.println(" >>     " + e.getMessage());
        }

        return studentsList;
    }

    public Student getStudent(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM students " +
                    "WHERE id=?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return new Student(resultSet.getInt("id"),
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
                resultList.add(new Student(resultSet.getInt("id"),
                        resultSet.getString("name")));
            }

            return resultList;
        } catch (SQLException e) {
            System.out.println(" >>     " + e.getMessage());
            return resultList;
        }
    }

    public int getMaxGlobalId() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM students");

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

    public boolean updateStudent(int id, Student updatedStudent) {
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

    public boolean deleteStudent(int id) {
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
}
