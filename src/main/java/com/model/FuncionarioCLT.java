package com.model;

public class FuncionarioCLT extends FuncionarioBase {

    public FuncionarioCLT() {
        super();
    }

    public FuncionarioCLT(int id, String nome, int empresaId) {
        super(id, nome, empresaId);
    }

    public FuncionarioCLT(String nome, int empresaId) {
        super(nome, empresaId);
    }

    @Override
    public double calcularSalario() {
        // Regra de negócio para CLT
        return 3000.0;
    }
}
