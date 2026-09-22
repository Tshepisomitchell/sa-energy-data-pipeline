package za.co.tshepiso.energy.repository;

import za.co.tshepiso.energy.model.EnergyReading;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcEnergyReadingRepository
        implements EnergyReadingRepository {

    private static final String DATABASE_URL =
            "jdbc:sqlite:energy_pipeline.db";

    @Override
    public void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS energy_reading (
                    reading_id INTEGER PRIMARY KEY,
                    household_id TEXT NOT NULL,
                    reading_date TEXT NOT NULL,
                    usage_kwh REAL NOT NULL CHECK (usage_kwh >= 0),
                    cost_per_kwh REAL NOT NULL CHECK (cost_per_kwh > 0),
                    municipality TEXT NOT NULL
                )
                """;

        try (
                Connection connection =
                        DriverManager.getConnection(DATABASE_URL);
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        }
    }

    @Override
    public void saveAll(List<EnergyReading> readings)
            throws SQLException {

        String sql = """
                INSERT OR REPLACE INTO energy_reading (
                    reading_id,
                    household_id,
                    reading_date,
                    usage_kwh,
                    cost_per_kwh,
                    municipality
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DriverManager.getConnection(DATABASE_URL)) {

            connection.setAutoCommit(false);

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                for (EnergyReading reading : readings) {
                    statement.setInt(
                            1,
                            reading.getReadingId()
                    );
                    statement.setString(
                            2,
                            reading.getHouseholdId()
                    );
                    statement.setString(
                            3,
                            reading.getReadingDate().toString()
                    );
                    statement.setDouble(
                            4,
                            reading.getUsageKwh()
                    );
                    statement.setDouble(
                            5,
                            reading.getCostPerKwh()
                    );
                    statement.setString(
                            6,
                            reading.getMunicipality()
                    );

                    statement.addBatch();
                }

                statement.executeBatch();
                connection.commit();

            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public List<EnergyReading> findAll() throws SQLException {
        String sql = """
                SELECT
                    reading_id,
                    household_id,
                    reading_date,
                    usage_kwh,
                    cost_per_kwh,
                    municipality
                FROM energy_reading
                ORDER BY reading_date, reading_id
                """;

        List<EnergyReading> readings = new ArrayList<>();

        try (
                Connection connection =
                        DriverManager.getConnection(DATABASE_URL);
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                EnergyReading reading = new EnergyReading(
                        resultSet.getInt("reading_id"),
                        resultSet.getString("household_id"),
                        LocalDate.parse(
                                resultSet.getString("reading_date")
                        ),
                        resultSet.getDouble("usage_kwh"),
                        resultSet.getDouble("cost_per_kwh"),
                        resultSet.getString("municipality")
                );

                readings.add(reading);
            }
        }

        return readings;
    }

    @Override
    public int count() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total
                FROM energy_reading
                """;

        try (
                Connection connection =
                        DriverManager.getConnection(DATABASE_URL);
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            return resultSet.getInt("total");
        }
    }

    @Override
    public double calculateTotalUsage() throws SQLException {
        String sql = """
                SELECT COALESCE(SUM(usage_kwh), 0) AS total_usage
                FROM energy_reading
                """;

        try (
                Connection connection =
                        DriverManager.getConnection(DATABASE_URL);
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            return resultSet.getDouble("total_usage");
        }
    }

    @Override
    public double calculateTotalCost() throws SQLException {
        String sql = """
                SELECT
                    COALESCE(
                        SUM(usage_kwh * cost_per_kwh),
                        0
                    ) AS total_cost
                FROM energy_reading
                """;

        try (
                Connection connection =
                        DriverManager.getConnection(DATABASE_URL);
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            return resultSet.getDouble("total_cost");
        }
    }
}