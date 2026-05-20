package com.fitnessclub.dao;

import com.fitnessclub.enums.Specialitate;
import com.fitnessclub.exceptions.DatabaseException;
import com.fitnessclub.model.Antrenor;
import com.fitnessclub.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntrenorDAO {

    private Connection getConn() {
        return DatabaseManager.getInstance().getConnection();
    }

    public void save(Antrenor a) {
        String sql = "INSERT INTO antrenori (nume, prenume, email, telefon, specialitate, salariu) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNume());
            ps.setString(2, a.getPrenume());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getTelefon());
            ps.setString(5, a.getSpecialitate().name());
            ps.setDouble(6, a.getSalariu());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) System.out.println("Antrenor salvat cu ID: " + keys.getInt(1));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la salvarea antrenorului: " + e.getMessage(), e);
        }
    }

    public List<Antrenor> findAll() {
        List<Antrenor> lista = new ArrayList<>();
        String sql = "SELECT * FROM antrenori";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la citirea antrenorilor: " + e.getMessage(), e);
        }
        return lista;
    }

    public Antrenor findById(int id) {
        String sql = "SELECT * FROM antrenori WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la căutarea antrenorului: " + e.getMessage(), e);
        }
        return null;
    }

    public void update(Antrenor a) {
        String sql = "UPDATE antrenori SET nume=?, prenume=?, email=?, telefon=?, specialitate=?, salariu=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, a.getNume());
            ps.setString(2, a.getPrenume());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getTelefon());
            ps.setString(5, a.getSpecialitate().name());
            ps.setDouble(6, a.getSalariu());
            ps.setInt(7, a.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la actualizarea antrenorului: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM antrenori WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la ștergerea antrenorului: " + e.getMessage(), e);
        }
    }

    public List<Antrenor> findBySpecialitate(Specialitate specialitate) {
        List<Antrenor> lista = new ArrayList<>();
        String sql = "SELECT * FROM antrenori WHERE specialitate = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, specialitate.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la filtrare: " + e.getMessage(), e);
        }
        return lista;
    }

    private Antrenor mapRow(ResultSet rs) throws SQLException {
        return new Antrenor(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                Specialitate.valueOf(rs.getString("specialitate")),
                rs.getDouble("salariu")
        );
    }
}