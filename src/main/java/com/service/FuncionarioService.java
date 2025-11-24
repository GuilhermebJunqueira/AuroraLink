package com.service;

import com.model.Funcionario;
import com.model.FuncionarioBase;
import com.model.FuncionarioCLT;
import com.model.FuncionarioPJ;
import com.repository.FuncionarioRepository;

import java.sql.SQLException;
import java.util.List;

public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    private final EmpresaService empresaService = new EmpresaService();

    public Funcionario create(Funcionario funcionario) throws SQLException {
        // Verifica se empresa existe
        if (!empresaService.existeEmpresa(funcionario.getEmpresaId())) {
            throw new IllegalArgumentException("Empresa inexistente para o funcionário");
        }

        if (funcionario.getNome() == null || funcionario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do funcionário é obrigatório");
        }

        if (funcionario.getTipo() == null ||
                (!funcionario.getTipo().equalsIgnoreCase("CLT")
                        && !funcionario.getTipo().equalsIgnoreCase("PJ"))) {
            throw new IllegalArgumentException("Tipo de funcionário deve ser 'CLT' ou 'PJ'");
        }

        // Polimorfismo para cálculo de salário
        FuncionarioBase base;
        if (funcionario.getTipo().equalsIgnoreCase("CLT")) {
            base = new FuncionarioCLT(funcionario.getNome(), funcionario.getEmpresaId());
        } else {
            base = new FuncionarioPJ(funcionario.getNome(), funcionario.getEmpresaId());
        }

        double salarioCalculado = base.calcularSalario();
        funcionario.setSalario(salarioCalculado);

        funcionarioRepository.create(funcionario);
        return funcionario;
    }

    public List<Funcionario> findAllByEmpresa(int empresaId) throws SQLException {
        if (!empresaService.existeEmpresa(empresaId)) {
            throw new IllegalArgumentException("Empresa inexistente");
        }
        return funcionarioRepository.findAllByEmpresa(empresaId);
    }

    public Funcionario findById(int id) throws SQLException {
        Funcionario f = funcionarioRepository.findById(id);
        if (f == null) {
            throw new IllegalArgumentException("Funcionário não encontrado");
        }
        return f;
    }

    public void update(int id, Funcionario funcionario) throws SQLException {
        Funcionario atual = findById(id);

        if (!empresaService.existeEmpresa(funcionario.getEmpresaId())) {
            throw new IllegalArgumentException("Empresa inexistente");
        }

        atual.setNome(funcionario.getNome());
        atual.setEmpresaId(funcionario.getEmpresaId());
        atual.setTipo(funcionario.getTipo());

        // recalcular salário via polimorfismo
        FuncionarioBase base;
        if (atual.getTipo().equalsIgnoreCase("CLT")) {
            base = new FuncionarioCLT(atual.getNome(), atual.getEmpresaId());
        } else {
            base = new FuncionarioPJ(atual.getNome(), atual.getEmpresaId());
        }
        atual.setSalario(base.calcularSalario());

        funcionarioRepository.update(atual);
    }

    public void delete(int id) throws SQLException {
        // garante que existe
        findById(id);
        funcionarioRepository.delete(id);
    }
}
