package com.model;

public class FuncionarioPJ extends FuncionarioBase {

    public FuncionarioPJ() {
        super();
    }

    public FuncionarioPJ(int id, String nome, int empresaId) {
        super(id, nome, empresaId);
    }

    public FuncionarioPJ(String nome, int empresaId) {
        super(nome, empresaId);
    }

    @Override
    public double calcularSalario() {
        // Regra de negócio para PJ
        return 5000.0;
    }
}
