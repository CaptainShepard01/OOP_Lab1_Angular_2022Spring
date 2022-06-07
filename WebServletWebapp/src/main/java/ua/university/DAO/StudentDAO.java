package ua.university.DAO;

import lombok.extern.slf4j.Slf4j;
import ua.university.config.DataSourceConfig;
import ua.university.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StudentDAO {
    private final Connection connection;

    public StudentDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    public List<Student> indexStudent() throws SQLException {
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
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }

        return studentsList;
    }

    public Student getStudent(int id) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM students " +
                    "WHERE id=?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Student(resultSet.getInt("id"),
                        resultSet.getString("name"));
            }

            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public List<Student> getStudent(String name) throws SQLException {
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
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public int getMaxGlobalId() throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM students");

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

    public int saveStudent(Student student) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students " +
                    "(name) " +
                    "VALUES(?) RETURNING id");
            connection.setAutoCommit(false);
            statement.setString(1, student.getName());
            statement.executeUpdate();
            statement.executeUpdate();

            ResultSet rs = statement.executeQuery();
            connection.commit();

            if(rs.next()){
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void updateStudent(int id, Student updatedStudent) throws SQLException {
        String sql1 = "SELECT id FROM students WHERE id=?";
        String sql2 = "UPDATE students SET name=? WHERE id=?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(2, id);
            } else {
                String error = String.format("Could not find a student with id: %d to update", id);
                log.error(error);
                throw new SQLException(error);
            }

            ps2.setString(1, updatedStudent.getName());
            ps2.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String sql1 = "SELECT id FROM students WHERE id=?";
        String sql2 = "DELETE FROM students WHERE id = ?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, id);
            } else {
                String error = String.format("Could not find a student with id: %d to delete", id);
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
