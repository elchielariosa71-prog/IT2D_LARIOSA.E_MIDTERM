package scc.dormitory.dao;

import scc.dormitory.model.ScenarioEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    public ScenarioDao() {
        createTable();
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS allocations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_name TEXT NOT NULL,
                    building TEXT NOT NULL,
                    room_no TEXT NOT NULL,
                    monthly_rent REAL NOT NULL
                )
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Could not create allocations table.", e);
        }
    }

    public boolean addAllocation(ScenarioEntity resident) {
        String sql = """
                INSERT INTO allocations
                (student_name, building, room_no, monthly_rent)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, resident.studentName());
            statement.setString(2, resident.building());
            statement.setString(3, resident.roomNo());
            statement.setDouble(4, resident.monthlyRent());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println(
                    "Unable to save allocation: " + e.getMessage());
            return false;
        }
    }

    public List<ScenarioEntity> getAllAllocations() {
        List<ScenarioEntity> residents = new ArrayList<>();

        String sql = """
                SELECT id, student_name, building,
                       room_no, monthly_rent
                FROM allocations
                ORDER BY id
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                residents.add(new ScenarioEntity(
                        result.getInt("id"),
                        result.getString("student_name"),
                        result.getString("building"),
                        result.getString("room_no"),
                        result.getDouble("monthly_rent")
                ));
            }

        } catch (SQLException e) {
            System.err.println(
                    "Unable to retrieve allocations: "
                            + e.getMessage());
        }

        return residents;
    }
}

