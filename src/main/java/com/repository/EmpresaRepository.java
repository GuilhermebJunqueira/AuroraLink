package com.repository;

import com.database.Database;
import com.model.Empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaRepository {

    public void create(Empresa empresa) throws SQLException {
        String sql = "INSERT INTO empresa (nome, cnpj) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getCnpj());
            stmt.executeUpdate();
        }
    }

    public List<Empresa> findAll() throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT id, nome, cnpj FROM empresa";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                empresas.add(new Empresa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj")
                ));
            }
        }
        return empresas;
    }

    public Empresa findById(int id) throws SQLException {
        String sql = "SELECT id, nome, cnpj FROM empresa WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Empresa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj")
                );
            }
            return null;
        }
    }

    public void update(Empresa empresa) throws SQLException {
        String sql = "UPDATE empresa SET nome = ?, cnpj = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getCnpj());
            stmt.setInt(3, empresa.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM empresa WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM empresa WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
