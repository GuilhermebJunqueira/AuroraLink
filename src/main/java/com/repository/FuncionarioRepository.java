package com.repository;

import com.util.Database;
import com.model.FuncionarioBase;
import com.model.FuncionarioCLT;
import com.model.FuncionarioPJ;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    public FuncionarioBase create(FuncionarioBase funcionario) throws SQLException {
        String sql = "INSERT INTO funcionario (nome, salario, tipo, empresa_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, funcionario.getNome());

            double salario = funcionario instanceof FuncionarioCLT
                    ? ((FuncionarioCLT) funcionario).getSalarioFixo()
                    : ((FuncionarioPJ) funcionario).getValorContrato();

            stmt.setDouble(2, salario);
            stmt.setString(3, funcionario instanceof FuncionarioCLT ? "CLT" : "PJ");
            stmt.setInt(4, funcionario.getEmpresaId());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    funcionario.setId(keys.getInt(1));
                }
            }
        }

        return funcionario;
    }

    public FuncionarioBase findById(int id) throws SQLException {
        String sql = "SELECT id, nome, salario, tipo, empresa_id FROM funcionario WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");
                    String nome = rs.getString("nome");
                    int empresaId = rs.getInt("empresa_id");
                    double salario = rs.getDouble("salario");

                    if ("CLT".equalsIgnoreCase(tipo)) {
                        FuncionarioCLT clt = new FuncionarioCLT(id, nome, empresaId);
                        clt.setSalarioFixo(salario);
                        return clt;
                    } else {
                        FuncionarioPJ pj = new FuncionarioPJ(id, nome, empresaId);
                        pj.setValorContrato(salario);
                        return pj;
                    }
                }
            }
        }

        return null;
    }

    public List<FuncionarioBase> findAll() throws SQLException {
        List<FuncionarioBase> list = new ArrayList<>();
        String sql = "SELECT id, nome, salario, tipo, empresa_id FROM funcionario";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String nome = rs.getString("nome");
                int empresaId = rs.getInt("empresa_id");
                double salario = rs.getDouble("salario");

                if ("CLT".equalsIgnoreCase(tipo)) {
                    FuncionarioCLT clt = new FuncionarioCLT(id, nome, empresaId);
                    clt.setSalarioFixo(salario);
                    list.add(clt);
                } else {
                    FuncionarioPJ pj = new FuncionarioPJ(id, nome, empresaId);
                    pj.setValorContrato(salario);
                    list.add(pj);
                }
            }
        }

        return list;
    }

    public List<FuncionarioBase> findAllByEmpresa(int empresaId) throws SQLException {
        List<FuncionarioBase> list = new ArrayList<>();
        String sql = "SELECT id, nome, salario, tipo FROM funcionario WHERE empresa_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empresaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String tipo = rs.getString("tipo");
                    String nome = rs.getString("nome");
                    double salario = rs.getDouble("salario");

                    if ("CLT".equalsIgnoreCase(tipo)) {
                        FuncionarioCLT clt = new FuncionarioCLT(id, nome, empresaId);
                        clt.setSalarioFixo(salario);
                        list.add(clt);
                    } else {
                        FuncionarioPJ pj = new FuncionarioPJ(id, nome, empresaId);
                        pj.setValorContrato(salario);
                        list.add(pj);
                    }
                }
            }
        }

        return list;
    }

    public boolean update(FuncionarioBase funcionario) throws SQLException {
        String sql = "UPDATE funcionario SET nome=?, salario=?, tipo=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());

            double salario = funcionario.calcularSalario();
            stmt.setDouble(2, salario);

            stmt.setDouble(2, salario);
            stmt.setString(3, funcionario instanceof FuncionarioCLT ? "CLT" : "PJ");
            stmt.setInt(4, funcionario.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM funcionario WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
