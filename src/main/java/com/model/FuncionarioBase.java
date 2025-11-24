package com.model;

public abstract class FuncionarioBase {

    protected int id;
    protected String nome;
    protected int empresaId;

    public FuncionarioBase() {
    }

    public FuncionarioBase(int id, String nome, int empresaId) {
        this.id = id;
        this.nome = nome;
        this.empresaId = empresaId;
    }

    public FuncionarioBase(String nome, int empresaId) {
        this.nome = nome;
        this.empresaId = empresaId;
    }

    public abstract double calcularSalario();

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) { this.nome = nome; }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) { this.empresaId = empresaId; }
}
