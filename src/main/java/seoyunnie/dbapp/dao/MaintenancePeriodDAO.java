package seoyunnie.dbapp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                return Optional.of(new MaintenancePeriod(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("hanger_id"),
                        rs.getString("status"),
                        rs.getDate("started_at"),
                        rs.getDate("completed_at")));
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
                periods.add(new MaintenancePeriod(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("hanger_id"),
                        rs.getString("status"),
                        rs.getDate("started_at"),
                        rs.getDate("completed_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return periods;
    }

    public void save(MaintenancePeriod period) {
        String q = "INSERT INTO maintenance_periods (id, type, aircraft_registration, hanger_id, status, started_at, completed_at) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, period.getId());
            stmt.setString(2, period.getType());
            stmt.setString(3, period.getAircraftRegistration());
            stmt.setInt(4, period.getHangerId());
            stmt.setString(5, period.getStatus().toString());
            stmt.setDate(6, Date.valueOf(period.getStartedAt()));
            stmt.setDate(7, period.getCompletedAt() != null ? Date.valueOf(period.getCompletedAt()) : null);

            stmt.executeUpdate();
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
