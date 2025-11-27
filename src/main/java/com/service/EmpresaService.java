package com.service;

import com.model.Empresa;
import com.repository.EmpresaRepository;

import java.sql.SQLException;
import java.util.List;

public class EmpresaService {

    private final EmpresaRepository empresaRepository = new EmpresaRepository();

    public Empresa create(Empresa empresa) throws SQLException {
        empresaRepository.create(empresa);
        return empresa;
    }

    public List<Empresa> findAll() throws SQLException {
        return empresaRepository.findAll();
    }

    public Empresa findById(int id) throws SQLException {
        Empresa e = empresaRepository.findById(id);
        if (e == null) {
            throw new IllegalArgumentException("Empresa não encontrada");
        }
        return e;
    }

    public void update(int id, Empresa empresa) throws SQLException {
        Empresa e = findById(id);
        e.setNome(empresa.getNome());
        e.setCnpj(empresa.getCnpj());
        empresaRepository.update(e);
    }

    public void delete(int id) throws SQLException {
        if (!empresaRepository.existsById(id)) {
            throw new IllegalArgumentException("Empresa não encontrada");
        }
        empresaRepository.delete(id);
    }

    public boolean existeEmpresa(int id) throws SQLException {
        return empresaRepository.existsById(id);
    }
}
