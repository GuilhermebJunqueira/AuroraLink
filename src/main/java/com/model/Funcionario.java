package com.model;

public class Funcionario {

    private int id;
    private String nome;
    private double salario;
    private String tipo;      // "CLT" ou "PJ"
    private int empresaId;

    public Funcionario() {
    }

    public Funcionario(int id, String nome, double salario, String tipo, int empresaId) {
        this.id = id;
        this.nome = nome;
        this.salario = salario;
        this.tipo = tipo;
        this.empresaId = empresaId;
    }

    public Funcionario(String nome, double salario, String tipo, int empresaId) {
        this.nome = nome;
        this.salario = salario;
        this.tipo = tipo;
        this.empresaId = empresaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) { this.nome = nome; }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) { this.salario = salario; }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) { this.empresaId = empresaId; }
}
