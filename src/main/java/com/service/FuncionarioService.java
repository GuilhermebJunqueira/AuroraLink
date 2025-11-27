package com.service;

import com.model.FuncionarioBase;
import com.repository.FuncionarioRepository;

import java.sql.SQLException;
import java.util.List;

public class FuncionarioService {

    private final FuncionarioRepository repo = new FuncionarioRepository();
    private final EmpresaService empresaService = new EmpresaService();

    public FuncionarioBase create(FuncionarioBase funcionario) throws SQLException {
        if (!empresaService.existeEmpresa(funcionario.getEmpresaId())) {
            throw new IllegalArgumentException("Empresa inexistente");
        }
        return repo.create(funcionario);
    }

    public FuncionarioBase findById(int id) throws SQLException {
        return repo.findById(id);
    }

    public List<FuncionarioBase> findAll() throws SQLException {
        return repo.findAll();
    }

    public List<FuncionarioBase> findAllByEmpresa(int empresaId) throws SQLException {
        return repo.findAllByEmpresa(empresaId);
    }

    public void update(int id, FuncionarioBase f) throws SQLException {
        f.setId(id);
        if (!repo.update(f)) {
            throw new SQLException("Falha ao atualizar");
        }
    }

    public boolean delete(int id) throws SQLException {
        return repo.delete(id);
    }
}
