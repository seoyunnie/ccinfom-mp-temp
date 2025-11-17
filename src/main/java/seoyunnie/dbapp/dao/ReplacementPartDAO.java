package seoyunnie.dbapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seoyunnie.dbapp.model.ReplacementPart;

public class ReplacementPartDAO {
    private final Connection connection;

    public ReplacementPartDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<ReplacementPart> get(int num, int maintenanceId) {
        String q = "SELECT * FROM replacement_parts WHERE number = ? AND maintenance_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, num);
            stmt.setInt(2, maintenanceId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new ReplacementPart(
                        rs.getInt("number"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("maintenance_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<ReplacementPart> getAll() {
        List<ReplacementPart> parts = new ArrayList<>();

        String q = "SELECT * FROM replacement_parts";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                parts.add(new ReplacementPart(
                        rs.getInt("number"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("maintenance_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parts;
    }

    public List<ReplacementPart> getAll(String registration) {
        List<ReplacementPart> parts = new ArrayList<>();

        String q = "SELECT * FROM replacement_parts WHERE aircraft_registration = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, registration);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                parts.add(new ReplacementPart(
                        rs.getInt("number"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("maintenance_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parts;
    }

    public List<ReplacementPart> getAll(int maintenanceId) {
        List<ReplacementPart> parts = new ArrayList<>();

        String q = "SELECT * FROM replacement_parts WHERE maintenance_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, maintenanceId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                parts.add(new ReplacementPart(
                        rs.getInt("number"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getString("aircraft_registration"),
                        rs.getInt("maintenance_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parts;
    }

    public void save(ReplacementPart part) {
        String q = "INSERT INTO replacement_parts (number, name, amount, aircraft_registration, maintenance_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, part.getNumber());
            stmt.setString(2, part.getName());
            stmt.setInt(3, part.getAmount());
            stmt.setString(4, part.getAircraftRegistration());
            stmt.setInt(5, part.getMaintenanceId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ReplacementPart part) {
        String q = "UPDATE replacement_parts SET amount = ? WHERE number = ? AND maintenance_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, part.getAmount());
            stmt.setInt(2, part.getNumber());
            stmt.setInt(3, part.getMaintenanceId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int num, int maintenanceId) {
        String q = "DELETE FROM replacement_parts WHERE number = ? AND maintenance_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, num);
            stmt.setInt(2, maintenanceId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
