package ua.university.DAO;

import lombok.extern.slf4j.Slf4j;
import ua.university.config.DataSourceConfig;
import ua.university.models.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TeacherDAO {
    private final Connection connection;

    public TeacherDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    public List<Teacher> indexTeacher() throws SQLException {
        List<Teacher> teachersList = new ArrayList<>();

        String sql = "SELECT * FROM teachers";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                teachersList.add(new Teacher(id, name));
            }
            resultSet.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }

        return teachersList;
    }

    public Teacher getTeacher(int id) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM teachers " +
                    "WHERE id=?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Teacher(resultSet.getInt("id"),
                        resultSet.getString("name"));
            }

            return null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public int getMaxGlobalId() throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM teachers");

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

    public void saveTeacher(Teacher teacher) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO teachers " +
                    "(name) " +
                    "VALUES(?)");
            connection.setAutoCommit(false);
            statement.setString(1, teacher.getName());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void updateTeacher(int id, Teacher updatedTeacher) throws SQLException {
        String sql1 = "SELECT id FROM teachers WHERE name=?";
        String sql2 = "UPDATE teachers SET name=? WHERE id=?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(2, id);
            } else {
                String error = String.format("Could not find a teacher with id: %d to update", id);
                log.error(error);
                throw new SQLException(error);
            }

            ps2.setString(1, updatedTeacher.getName());
            ps2.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
    }

    public void deleteTeacher(int id) throws SQLException {
        String sql1 = "SELECT id FROM teachers WHERE name=?";
        String sql2 = "DELETE from teachers WHERE id=?";

        try(PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2)) {
            connection.setAutoCommit(false);

            ps1.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                ps2.setInt(1, id);
            } else {
                String error = String.format("Could not find a teacher with id: %d to delete", id);
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
