package com.repository;

import com.database.Database;
import com.model.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    public void create(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionario (nome, salario, tipo, empresa_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setDouble(2, funcionario.getSalario());
            stmt.setString(3, funcionario.getTipo());
            stmt.setInt(4, funcionario.getEmpresaId());

            stmt.executeUpdate();
        }
    }

    public List<Funcionario> findAllByEmpresa(int empresaId) throws SQLException {
        String sql = "SELECT id, nome, salario, tipo, empresa_id FROM funcionario WHERE empresa_id = ?";
        List<Funcionario> funcionarios = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empresaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                funcionarios.add(map(rs));
            }
        }
        return funcionarios;
    }

    public Funcionario findById(int id) throws SQLException {
        String sql = "SELECT id, nome, salario, tipo, empresa_id FROM funcionario WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return map(rs);
            }
            return null;
        }
    }

    public void update(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE funcionario SET nome = ?, salario = ?, tipo = ?, empresa_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setDouble(2, funcionario.getSalario());
            stmt.setString(3, funcionario.getTipo());
            stmt.setInt(4, funcionario.getEmpresaId());
            stmt.setInt(5, funcionario.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM funcionario WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Funcionario map(ResultSet rs) throws SQLException {
        return new Funcionario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getDouble("salario"),
                rs.getString("tipo"),
                rs.getInt("empresa_id")
        );
    }
}
