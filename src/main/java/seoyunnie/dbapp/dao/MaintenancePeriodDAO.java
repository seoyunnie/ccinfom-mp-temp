package seoyunnie.dbapp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seoyunnie.dbapp.model.MaintenancePeriod;

public class MaintenancePeriodDAO {
    private final Connection connection;

    public MaintenancePeriodDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<MaintenancePeriod> get(Integer id) {
        String q = "SELECT * FROM maintenance_periods WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Date completedAt = rs.getDate("completed_at");

                return Optional.of(new MaintenancePeriod(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("hanger_id"),
                        rs.getString("status"),
                        rs.getDate("started_at").toLocalDate(),
                        completedAt != null ? completedAt.toLocalDate() : null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<MaintenancePeriod> getAll() {
        List<MaintenancePeriod> periods = new ArrayList<>();

        String q = "SELECT * FROM maintenance_periods";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Date completedAt = rs.getDate("completed_at");

                periods.add(new MaintenancePeriod(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("hanger_id"),
                        rs.getString("status"),
                        rs.getDate("started_at").toLocalDate(),
                        completedAt != null ? completedAt.toLocalDate() : null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return periods;
    }

    public List<MaintenancePeriod> getAll(String registration) {
        List<MaintenancePeriod> periods = new ArrayList<>();

        String q = "SELECT * FROM maintenance_periods WHERE aircraft_registration = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, registration);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Date completedAt = rs.getDate("completed_at");

                periods.add(new MaintenancePeriod(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("hanger_id"),
                        rs.getString("status"),
                        rs.getDate("started_at").toLocalDate(),
                        completedAt != null ? completedAt.toLocalDate() : null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return periods;
    }

    public List<MaintenancePeriod> getAll(int hangerId) {
        List<MaintenancePeriod> periods = new ArrayList<>();

        String q = "SELECT * FROM maintenance_periods WHERE hanger_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, hangerId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Date completedAt = rs.getDate("completed_at");

                periods.add(new MaintenancePeriod(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("hanger_id"),
                        rs.getString("status"),
                        rs.getDate("started_at").toLocalDate(),
                        completedAt != null ? completedAt.toLocalDate() : null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return periods;
    }

    public void save(MaintenancePeriod period) {
        String q = "INSERT INTO maintenance_periods (type, aircraft_registration, hanger_id, status, started_at, completed_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, period.getType());
            stmt.setString(2, period.getAircraftRegistration());
            stmt.setInt(3, period.getHangerId());
            stmt.setString(4, period.getStatus().toString());
            stmt.setDate(5, Date.valueOf(period.getStartedAt()));
            stmt.setDate(6, period.getCompletedAt() != null ? Date.valueOf(period.getCompletedAt()) : null);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                period.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(MaintenancePeriod period) {
        if (period.getCompletedAt() == null) {
            return;
        }

        String q = "UPDATE maintenance_periods SET completed_at = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setDate(1, Date.valueOf(period.getCompletedAt()));
            stmt.setInt(2, period.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer id) {
        String q = "DELETE FROM maintenance_periods WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
