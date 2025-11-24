package com.service;

import com.model.Empresa;
import com.repository.EmpresaRepository;

import java.sql.SQLException;
import java.util.List;

public class EmpresaService {

    private final EmpresaRepository empresaRepository = new EmpresaRepository();

    public Empresa create(Empresa empresa) throws SQLException {
        if (empresa.getNome() == null || empresa.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da empresa é obrigatório");
        }
        if (empresa.getCnpj() == null || empresa.getCnpj().isBlank()) {
            throw new IllegalArgumentException("CNPJ da empresa é obrigatório");
        }

        empresaRepository.create(empresa);
        // Poderia retornar com ID gerado, mas para simplificar vamos só retornar o objeto.
        return empresa;
    }

    public List<Empresa> findAll() throws SQLException {
        return empresaRepository.findAll();
    }

    public Empresa findById(int id) throws SQLException {
        Empresa empresa = empresaRepository.findById(id);
        if (empresa == null) {
            throw new IllegalArgumentException("Empresa não encontrada");
        }
        return empresa;
    }

    public void update(int id, Empresa empresa) throws SQLException {
        Empresa atual = findById(id); // lança exceção se não existir
        atual.setNome(empresa.getNome());
        atual.setCnpj(empresa.getCnpj());
        empresaRepository.update(atual);
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
