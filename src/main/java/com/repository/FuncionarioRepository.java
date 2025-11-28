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
        String sql = """
            INSERT INTO funcionario (
                nome, salario, tipo, empresa_id,
                salario_fixo, beneficios, vale_transporte, plano_saude, bonus_anual,
                valor_contrato, qtd_horas_mensais, valor_hora_extra, tempo_projeto_meses, taxa_servico_percentual
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setDouble(2, funcionario.calcularSalario());
            stmt.setString(3, funcionario instanceof FuncionarioCLT ? "CLT" : "PJ");
            stmt.setInt(4, funcionario.getEmpresaId());

            if (funcionario instanceof FuncionarioCLT clt) {
                stmt.setDouble(5, clt.getSalarioFixo());
                stmt.setDouble(6, clt.getBeneficios());
                stmt.setDouble(7, clt.getValeTransporte());
                stmt.setDouble(8, clt.getPlanoSaude());
                stmt.setDouble(9, clt.getBonusAnual());

                stmt.setNull(10, Types.REAL);
                stmt.setNull(11, Types.INTEGER);
                stmt.setNull(12, Types.REAL);
                stmt.setNull(13, Types.INTEGER);
                stmt.setNull(14, Types.REAL);
            } else if (funcionario instanceof FuncionarioPJ pj) {
                stmt.setNull(5, Types.REAL);
                stmt.setNull(6, Types.REAL);
                stmt.setNull(7, Types.REAL);
                stmt.setNull(8, Types.REAL);
                stmt.setNull(9, Types.REAL);

                stmt.setDouble(10, pj.getValorContrato());
                stmt.setInt(11, pj.getQtdHorasMensais());
                stmt.setDouble(12, pj.getValorHoraExtra());
                stmt.setInt(13, pj.getTempoProjetoMeses());
                stmt.setDouble(14, pj.getTaxaServicoPercentual());
            } else {
                // fallback bem defensivo, não deveria cair aqui
                for (int i = 5; i <= 14; i++) {
                    stmt.setNull(i, Types.REAL);
                }
            }

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
        String sql = """
            SELECT
                id, nome, salario, tipo, empresa_id,
                salario_fixo, beneficios, vale_transporte, plano_saude, bonus_anual,
                valor_contrato, qtd_horas_mensais, valor_hora_extra, tempo_projeto_meses, taxa_servico_percentual
            FROM funcionario WHERE id = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToFuncionario(rs);
                }
            }
        }
        return null;
    }

    public List<FuncionarioBase> findAll() throws SQLException {
        List<FuncionarioBase> list = new ArrayList<>();

        String sql = """
            SELECT
                id, nome, salario, tipo, empresa_id,
                salario_fixo, beneficios, vale_transporte, plano_saude, bonus_anual,
                valor_contrato, qtd_horas_mensais, valor_hora_extra, tempo_projeto_meses, taxa_servico_percentual
            FROM funcionario
        """;

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapRowToFuncionario(rs));
            }
        }

        return list;
    }

    public List<FuncionarioBase> findAllByEmpresa(int empresaId) throws SQLException {
        List<FuncionarioBase> list = new ArrayList<>();

        String sql = """
            SELECT
                id, nome, salario, tipo, empresa_id,
                salario_fixo, beneficios, vale_transporte, plano_saude, bonus_anual,
                valor_contrato, qtd_horas_mensais, valor_hora_extra, tempo_projeto_meses, taxa_servico_percentual
            FROM funcionario
            WHERE empresa_id = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, empresaId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToFuncionario(rs));
                }
            }
        }

        return list;
    }

    public boolean update(FuncionarioBase funcionario) throws SQLException {
        String sql = """
            UPDATE funcionario
            SET nome = ?, salario = ?, tipo = ?, 
                salario_fixo = ?, beneficios = ?, vale_transporte = ?, plano_saude = ?, bonus_anual = ?,
                valor_contrato = ?, qtd_horas_mensais = ?, valor_hora_extra = ?, tempo_projeto_meses = ?, taxa_servico_percentual = ?
            WHERE id = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setDouble(2, funcionario.calcularSalario());
            stmt.setString(3, funcionario instanceof FuncionarioCLT ? "CLT" : "PJ");

            if (funcionario instanceof FuncionarioCLT clt) {
                stmt.setDouble(4, clt.getSalarioFixo());
                stmt.setDouble(5, clt.getBeneficios());
                stmt.setDouble(6, clt.getValeTransporte());
                stmt.setDouble(7, clt.getPlanoSaude());
                stmt.setDouble(8, clt.getBonusAnual());

                stmt.setNull(9, Types.REAL);
                stmt.setNull(10, Types.INTEGER);
                stmt.setNull(11, Types.REAL);
                stmt.setNull(12, Types.INTEGER);
                stmt.setNull(13, Types.REAL);
            } else if (funcionario instanceof FuncionarioPJ pj) {
                stmt.setNull(4, Types.REAL);
                stmt.setNull(5, Types.REAL);
                stmt.setNull(6, Types.REAL);
                stmt.setNull(7, Types.REAL);
                stmt.setNull(8, Types.REAL);

                stmt.setDouble(9, pj.getValorContrato());
                stmt.setInt(10, pj.getQtdHorasMensais());
                stmt.setDouble(11, pj.getValorHoraExtra());
                stmt.setInt(12, pj.getTempoProjetoMeses());
                stmt.setDouble(13, pj.getTaxaServicoPercentual());
            } else {
                for (int i = 4; i <= 13; i++) {
                    stmt.setNull(i, Types.REAL);
                }
            }

            stmt.setInt(14, funcionario.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM funcionario WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // --- helper para reaproveitar o mapeamento em todos os SELECTs
    private FuncionarioBase mapRowToFuncionario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tipo = rs.getString("tipo");
        String nome = rs.getString("nome");
        int empresaId = rs.getInt("empresa_id");

        if ("CLT".equalsIgnoreCase(tipo)) {
            double salarioFixo    = rs.getDouble("salario_fixo");
            double beneficios     = rs.getDouble("beneficios");
            double valeTransporte = rs.getDouble("vale_transporte");
            double planoSaude     = rs.getDouble("plano_saude");
            double bonusAnual     = rs.getDouble("bonus_anual");

            return new FuncionarioCLT(id, nome, empresaId,
                    salarioFixo, beneficios, valeTransporte, planoSaude, bonusAnual);
        } else {
            double valorContrato         = rs.getDouble("valor_contrato");
            int qtdHorasMensais          = rs.getInt("qtd_horas_mensais");
            double valorHoraExtra        = rs.getDouble("valor_hora_extra");
            int tempoProjetoMeses        = rs.getInt("tempo_projeto_meses");
            double taxaServicoPercentual = rs.getDouble("taxa_servico_percentual");

            return new FuncionarioPJ(id, nome, empresaId,
                    valorContrato, qtdHorasMensais, valorHoraExtra, tempoProjetoMeses, taxaServicoPercentual);
        }
    }
}
