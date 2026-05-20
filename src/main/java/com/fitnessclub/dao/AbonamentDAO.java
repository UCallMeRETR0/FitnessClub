package com.fitnessclub.dao;

import com.fitnessclub.enums.StatusAbonament;
import com.fitnessclub.enums.TipAbonament;
import com.fitnessclub.exceptions.DatabaseException;
import com.fitnessclub.model.Abonament;
import com.fitnessclub.model.Client;
import com.fitnessclub.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonamentDAO {

    private Connection getConn() {
        return DatabaseManager.getInstance().getConnection();
    }

    public void save(Abonament a) {
        String sql = "INSERT INTO abonamente (tip, status, data_start, data_expirare, pret, client_id) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getTip().name());
            ps.setString(2, a.getStatus().name());
            ps.setDate(3, Date.valueOf(a.getDataStart()));
            ps.setDate(4, Date.valueOf(a.getDataExpirare()));
            ps.setDouble(5, a.getPret());
            ps.setInt(6, a.getClient().getId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) System.out.println("Abonament salvat cu ID: " + keys.getInt(1));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la salvarea abonamentului: " + e.getMessage(), e);
        }
    }

    public List<Abonament> findAll() {
        List<Abonament> lista = new ArrayList<>();
        String sql = "SELECT a.*, c.nume, c.prenume, c.email, c.telefon, c.data_nasterii " +
                "FROM abonamente a JOIN clienti c ON a.client_id = c.id";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la citirea abonamentelor: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Abonament> findByClientId(int clientId) {
        List<Abonament> lista = new ArrayList<>();
        String sql = "SELECT a.*, c.nume, c.prenume, c.email, c.telefon, c.data_nasterii " +
                "FROM abonamente a JOIN clienti c ON a.client_id = c.id WHERE a.client_id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la citire: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Abonament> findByStatus(StatusAbonament status) {
        List<Abonament> lista = new ArrayList<>();
        String sql = "SELECT a.*, c.nume, c.prenume, c.email, c.telefon, c.data_nasterii " +
                "FROM abonamente a JOIN clienti c ON a.client_id = c.id WHERE a.status = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la filtrare: " + e.getMessage(), e);
        }
        return lista;
    }

    public void update(Abonament a) {
        String sql = "UPDATE abonamente SET tip=?, status=?, data_start=?, data_expirare=?, pret=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, a.getTip().name());
            ps.setString(2, a.getStatus().name());
            ps.setDate(3, Date.valueOf(a.getDataStart()));
            ps.setDate(4, Date.valueOf(a.getDataExpirare()));
            ps.setDouble(5, a.getPret());
            ps.setInt(6, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la actualizarea abonamentului: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM abonamente WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la ștergerea abonamentului: " + e.getMessage(), e);
        }
    }

    private Abonament mapRow(ResultSet rs) throws SQLException {
        Client client = new Client(
                rs.getInt("client_id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                rs.getDate("data_nasterii").toLocalDate()
        );
        Abonament ab = new Abonament(
                rs.getInt("id"),
                TipAbonament.valueOf(rs.getString("tip")),
                rs.getDate("data_start").toLocalDate(),
                rs.getDouble("pret"),
                client
        );
        ab.setStatus(StatusAbonament.valueOf(rs.getString("status")));
        return ab;
    }
}