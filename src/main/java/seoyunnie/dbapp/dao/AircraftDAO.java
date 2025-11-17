package seoyunnie.dbapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seoyunnie.dbapp.model.Aircraft;

public class AircraftDAO {
    private final Connection connection;

    public AircraftDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Aircraft> get(String registration) {
        String q = "SELECT * FROM fleet WHERE registration = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, registration);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(
                        new Aircraft(rs.getString("registration"), rs.getString("model"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<Aircraft> getAll() {
        List<Aircraft> fleet = new ArrayList<>();

        String q = "SELECT * FROM fleet";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                fleet.add(new Aircraft(rs.getString("registration"), rs.getString("model"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fleet;
    }

    public void save(Aircraft aircraft) {
        String q = "INSERT INTO fleet (registration, model, status) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, aircraft.getRegistration());
            stmt.setString(2, aircraft.getModel());
            stmt.setString(3, aircraft.getStatus().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Aircraft aircraft) {
        String q = "UPDATE fleet SET status = ? WHERE registration = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, aircraft.getStatus().toString());
            stmt.setString(2, aircraft.getRegistration());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String registration) {
        String q = "DELETE FROM fleet WHERE registration = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, registration);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
