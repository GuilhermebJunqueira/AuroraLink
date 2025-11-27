package com.model;

public class FuncionarioPJ extends FuncionarioBase {

    private double valorContrato;
    private int qtdHorasMensais;
    private double valorHoraExtra;
    private int tempoProjetoMeses;
    private double taxaServicoPercentual;

    public FuncionarioPJ() {
        super();
    }

    public FuncionarioPJ(int id, String nome, int empresaId) {
        super(id, nome, empresaId);
        this.valorContrato = 5000.0;
        this.qtdHorasMensais = 160;
        this.valorHoraExtra = 60.0;
        this.tempoProjetoMeses = 12;
        this.taxaServicoPercentual = 10.0;
    }

    public FuncionarioPJ(String nome, int empresaId) {
        super(nome, empresaId);
        this.valorContrato = 5000.0;
        this.qtdHorasMensais = 160;
        this.valorHoraExtra = 60.0;
        this.tempoProjetoMeses = 12;
        this.taxaServicoPercentual = 10.0;
    }

    public FuncionarioPJ(int id, String nome, int empresaId, double valorContrato, int qtdHorasMensais,
                         double valorHoraExtra, int tempoProjetoMeses, double taxaServicoPercentual) {
        super(id, nome, empresaId);
        this.valorContrato = valorContrato;
        this.qtdHorasMensais = qtdHorasMensais;
        this.valorHoraExtra = valorHoraExtra;
        this.tempoProjetoMeses = tempoProjetoMeses;
        this.taxaServicoPercentual = taxaServicoPercentual;
    }

    @Override
    public double calcularSalario() {
        // Contrato - desconto de taxa de serviço
        return valorContrato - (valorContrato * (taxaServicoPercentual / 100));
    }

    public double getValorContrato() {
        return valorContrato;
    }

    public void setValorContrato(double valorContrato) {
        this.valorContrato = valorContrato;
    }

    public int getQtdHorasMensais() {
        return qtdHorasMensais;
    }

    public void setQtdHorasMensais(int qtdHorasMensais) {
        this.qtdHorasMensais = qtdHorasMensais;
    }

    public double getValorHoraExtra() {
        return valorHoraExtra;
    }

    public void setValorHoraExtra(double valorHoraExtra) {
        this.valorHoraExtra = valorHoraExtra;
    }

    public int getTempoProjetoMeses() {
        return tempoProjetoMeses;
    }

    public void setTempoProjetoMeses(int tempoProjetoMeses) {
        this.tempoProjetoMeses = tempoProjetoMeses;
    }

    public double getTaxaServicoPercentual() {
        return taxaServicoPercentual;
    }

    public void setTaxaServicoPercentual(double taxaServicoPercentual) {
        this.taxaServicoPercentual = taxaServicoPercentual;
    }
}
