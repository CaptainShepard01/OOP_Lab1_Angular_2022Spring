package ua.university.DAO;

import ua.university.config.DataSourceConfig;
import ua.university.models.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    private final Connection connection;

    public TeacherDAO() throws ClassNotFoundException, SQLException {
        connection = new DataSourceConfig().getConnection();
    }

    public void stop() throws SQLException {
        connection.close();
    }

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

    public int getMaxGlobalId() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT max(id) FROM teachers");

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
}
