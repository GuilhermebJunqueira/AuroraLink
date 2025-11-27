package com.model;

public class FuncionarioCLT extends FuncionarioBase {

    private double salarioFixo;
    private double beneficios;
    private double valeTransporte;
    private double planoSaude;
    private double bonusAnual;

    public FuncionarioCLT() {
        super();
    }

    public FuncionarioCLT(int id, String nome, int empresaId) {
        super(id, nome, empresaId);
        this.salarioFixo = 3000.0;
        this.beneficios = 0.0;
        this.valeTransporte = 250.0;
        this.planoSaude = 400.0;
        this.bonusAnual = 1200.0;
    }

    public FuncionarioCLT(String nome, int empresaId) {
        super(nome, empresaId);
        this.salarioFixo = 3000.0;
        this.beneficios = 0.0;
        this.valeTransporte = 250.0;
        this.planoSaude = 400.0;
        this.bonusAnual = 1200.0;
    }

    public FuncionarioCLT(int id, String nome, int empresaId, double salarioFixo, double beneficios,
                          double valeTransporte, double planoSaude, double bonusAnual) {
        super(id, nome, empresaId);
        this.salarioFixo = salarioFixo;
        this.beneficios = beneficios;
        this.valeTransporte = valeTransporte;
        this.planoSaude = planoSaude;
        this.bonusAnual = bonusAnual;
    }

    @Override
    public double calcularSalario() {
        // Salário mensal fixo + benefícios + rateio do bônus anual
        return salarioFixo + beneficios + valeTransporte + planoSaude + (bonusAnual / 12);
    }

    public double getSalarioFixo() {
        return salarioFixo;
    }

    public void setSalarioFixo(double salarioFixo) {
        this.salarioFixo = salarioFixo;
    }

    public double getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(double beneficios) {
        this.beneficios = beneficios;
    }

    public double getValeTransporte() {
        return valeTransporte;
    }

    public void setValeTransporte(double valeTransporte) {
        this.valeTransporte = valeTransporte;
    }

    public double getPlanoSaude() {
        return planoSaude;
    }

    public void setPlanoSaude(double planoSaude) {
        this.planoSaude = planoSaude;
    }

    public double getBonusAnual() {
        return bonusAnual;
    }

    public void setBonusAnual(double bonusAnual) {
        this.bonusAnual = bonusAnual;
    }
}
