package com.fitnessclub.dao;

import com.fitnessclub.exceptions.DatabaseException;
import com.fitnessclub.model.Client;
import com.fitnessclub.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    private Connection getConn() {
        return DatabaseManager.getInstance().getConnection();
    }

    public void save(Client c) {
        String sql = "INSERT INTO clienti (nume, prenume, email, telefon, data_nasterii) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNume());
            ps.setString(2, c.getPrenume());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefon());
            ps.setDate(5, Date.valueOf(c.getDataNasterii()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) System.out.println("Client salvat cu ID: " + keys.getInt(1));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la salvarea clientului: " + e.getMessage(), e);
        }
    }

    public List<Client> findAll() {
        List<Client> lista = new ArrayList<>();
        String sql = "SELECT * FROM clienti";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la citirea clienților: " + e.getMessage(), e);
        }
        return lista;
    }

    public Client findById(int id) {
        String sql = "SELECT * FROM clienti WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la căutarea clientului: " + e.getMessage(), e);
        }
        return null;
    }

    public void update(Client c) {
        String sql = "UPDATE clienti SET nume=?, prenume=?, email=?, telefon=?, data_nasterii=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, c.getNume());
            ps.setString(2, c.getPrenume());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefon());
            ps.setDate(5, Date.valueOf(c.getDataNasterii()));
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la actualizarea clientului: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM clienti WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la ștergerea clientului: " + e.getMessage(), e);
        }
    }

    public List<Client> findByNume(String nume) {
        List<Client> lista = new ArrayList<>();
        String sql = "SELECT * FROM clienti WHERE LOWER(nume) LIKE ? OR LOWER(prenume) LIKE ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            String pattern = "%" + nume.toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la căutare: " + e.getMessage(), e);
        }
        return lista;
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                rs.getDate("data_nasterii").toLocalDate()
        );
    }
}