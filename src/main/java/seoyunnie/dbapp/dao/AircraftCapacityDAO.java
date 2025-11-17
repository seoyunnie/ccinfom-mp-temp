package seoyunnie.dbapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seoyunnie.dbapp.model.AircraftCapacity;

public class AircraftCapacityDAO {
    private final Connection connection;

    public AircraftCapacityDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<AircraftCapacity> get(String model) {
        String q = "SELECT * FROM aircraft_capacities WHERE model = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, model);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new AircraftCapacity(rs.getString("model"), rs.getInt("capacity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<AircraftCapacity> getAll() {
        List<AircraftCapacity> capacities = new ArrayList<>();

        String q = "SELECT * FROM aircraft_capacities";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                capacities.add(new AircraftCapacity(rs.getString("model"), rs.getInt("capacity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return capacities;
    }

    public void save(AircraftCapacity aircraftCapacity) {
        String q = "INSERT INTO aircraft_capacities (model, capacity) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, aircraftCapacity.getModel());
            stmt.setInt(2, aircraftCapacity.getCapacity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(AircraftCapacity aircraftCapacity) {
        String q = "UPDATE aircraft_capacities SET capacity = ? WHERE model = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, aircraftCapacity.getCapacity());
            stmt.setString(2, aircraftCapacity.getModel());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String model) {
        String q = "DELETE FROM aircraft_capacities WHERE model = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, model);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
