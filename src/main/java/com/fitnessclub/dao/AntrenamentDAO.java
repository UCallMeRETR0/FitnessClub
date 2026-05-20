package com.fitnessclub.dao;

import com.fitnessclub.enums.Specialitate;
import com.fitnessclub.exceptions.DatabaseException;
import com.fitnessclub.model.Antrenament;
import com.fitnessclub.model.Antrenor;
import com.fitnessclub.model.Client;
import com.fitnessclub.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AntrenamentDAO {

    private Connection getConn() {
        return DatabaseManager.getInstance().getConnection();
    }

    public void save(Antrenament ant) {
        String sql = "INSERT INTO antrenamente (denumire, data, durata, capacitate, antrenor_id) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ant.getDenumire());
            ps.setTimestamp(2, Timestamp.valueOf(ant.getData()));
            ps.setInt(3, ant.getDurata());
            ps.setInt(4, ant.getCapacitate());
            ps.setInt(5, ant.getAntrenor().getId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int newId = keys.getInt(1);
                System.out.println("Antrenament salvat cu ID: " + newId);
                for (Client c : ant.getParticipanti()) {
                    addParticipant(newId, c.getId());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la salvarea antrenamentului: " + e.getMessage(), e);
        }
    }

    public void addParticipant(int antrenamentId, int clientId) {
        String sql = "IF NOT EXISTS (SELECT 1 FROM antrenament_participanti WHERE antrenament_id=? AND client_id=?) " +
                "INSERT INTO antrenament_participanti (antrenament_id, client_id) VALUES (?,?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, antrenamentId);
            ps.setInt(2, clientId);
            ps.setInt(3, antrenamentId);
            ps.setInt(4, clientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la adăugarea participantului: " + e.getMessage(), e);
        }
    }

    public void removeParticipant(int antrenamentId, int clientId) {
        String sql = "DELETE FROM antrenament_participanti WHERE antrenament_id=? AND client_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, antrenamentId);
            ps.setInt(2, clientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la ștergerea participantului: " + e.getMessage(), e);
        }
    }

    public List<Antrenament> findAll() {
        List<Antrenament> lista = new ArrayList<>();
        String sql = "SELECT ant.*, a.nume, a.prenume, a.email, a.telefon, a.specialitate, a.salariu " +
                "FROM antrenamente ant JOIN antrenori a ON ant.antrenor_id = a.id";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la citirea antrenamentelor: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Antrenament> findByData(LocalDate data) {
        List<Antrenament> lista = new ArrayList<>();
        String sql = "SELECT ant.*, a.nume, a.prenume, a.email, a.telefon, a.specialitate, a.salariu " +
                "FROM antrenamente ant JOIN antrenori a ON ant.antrenor_id = a.id " +
                "WHERE CAST(ant.data AS DATE) = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(data));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la filtrare dupa data: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(Antrenament ant) {
        String sql = "UPDATE antrenamente SET denumire=?, data=?, durata=?, capacitate=?, antrenor_id=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, ant.getDenumire());
            ps.setTimestamp(2, Timestamp.valueOf(ant.getData()));
            ps.setInt(3, ant.getDurata());
            ps.setInt(4, ant.getCapacitate());
            ps.setInt(5, ant.getAntrenor().getId());
            ps.setInt(6, ant.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la actualizarea antrenamentului: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM antrenamente WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la ștergerea antrenamentului: " + e.getMessage(), e);
        }
    }

    private Antrenament mapRow(ResultSet rs) throws SQLException {
        Antrenor antrenor = new Antrenor(
                rs.getInt("antrenor_id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                Specialitate.valueOf(rs.getString("specialitate")),
                rs.getDouble("salariu")
        );
        return new Antrenament(
                rs.getInt("id"),
                rs.getString("denumire"),
                rs.getTimestamp("data").toLocalDateTime(),
                rs.getInt("durata"),
                rs.getInt("capacitate"),
                antrenor
        );
    }
}