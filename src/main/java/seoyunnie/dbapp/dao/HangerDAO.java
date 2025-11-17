package seoyunnie.dbapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seoyunnie.dbapp.model.Hanger;

public class HangerDAO {
    private final Connection connection;

    public HangerDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Hanger> get(Integer id) {
        String q = "SELECT * FROM hangers WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Hanger(rs.getInt("id"), rs.getString("location"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public List<Hanger> getAll() {
        List<Hanger> hangers = new ArrayList<>();

        String q = "SELECT * FROM hangers";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                hangers.add(new Hanger(rs.getInt("id"), rs.getString("location"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hangers;
    }

    public void save(Hanger hanger) {
        String q = "INSERT INTO hangers (location, status) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, hanger.getLocation());
            stmt.setString(2, hanger.getStatus().toString());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                hanger.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Hanger hanger) {
        String q = "UPDATE hangers SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setString(1, hanger.getStatus().toString());
            stmt.setInt(2, hanger.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer id) {
        String q = "DELETE FROM hangers WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
