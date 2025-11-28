package com.util;

import com.model.Empresa;
import com.model.FuncionarioBase;
import com.model.FuncionarioCLT;
import com.model.FuncionarioPJ;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonUtil {

    public static String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static void writeJson(HttpExchange exchange, int code, String json) throws IOException {
        byte[] b = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(code, b.length);
        exchange.getResponseBody().write(b);
        exchange.getResponseBody().close();
    }

    // ========= EMPRESA =========

    public static Empresa fromJsonEmpresa(String json) {
        Empresa e = new Empresa();
        e.setNome(getJsonValue(json, "nome"));
        e.setCnpj(getJsonValue(json, "cnpj"));
        return e;
    }

    public static String toJson(Empresa e) {
        return "{"
                + "\"id\":" + e.getId() + ","
                + "\"nome\":\"" + escape(e.getNome()) + "\","
                + "\"cnpj\":\"" + escape(e.getCnpj()) + "\""
                + "}";
    }

    public static String toJsonEmpresas(List<Empresa> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (Empresa e : lista) {
            sb.append(toJson(e)).append(",");
        }
        if (!lista.isEmpty()) sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    // ========= FUNCIONÁRIO =========

    public static FuncionarioBase fromJsonFuncionario(String json) {
        String tipo = getJsonValue(json, "tipo");   // usa nosso parser
        String nome = getJsonValue(json, "nome");
        int empresaId = getJsonInt(json, "empresaId");

        if (tipo == null) tipo = "PJ"; // fallback seguro

        if (tipo.equalsIgnoreCase("CLT")) {
            FuncionarioCLT f = new FuncionarioCLT();
            f.setNome(nome);
            f.setEmpresaId(empresaId);

            double salarioFixo     = getJsonDouble(json, "salarioFixo");
            double beneficios      = getJsonDouble(json, "beneficios");
            double valeTransporte  = getJsonDouble(json, "valeTransporte");
            double planoSaude      = getJsonDouble(json, "planoSaude");
            double bonusAnual      = getJsonDouble(json, "bonusAnual");

            if (salarioFixo     > 0) f.setSalarioFixo(salarioFixo);
            if (beneficios      > 0) f.setBeneficios(beneficios);
            if (valeTransporte  > 0) f.setValeTransporte(valeTransporte);
            if (planoSaude      > 0) f.setPlanoSaude(planoSaude);
            if (bonusAnual      > 0) f.setBonusAnual(bonusAnual);

            return f;
        } else {
            FuncionarioPJ f = new FuncionarioPJ();
            f.setNome(nome);
            f.setEmpresaId(empresaId);

            double valorContrato         = getJsonDouble(json, "valorContrato");
            int qtdHorasMensais          = getJsonInt(json, "qtdHorasMensais");
            double valorHoraExtra        = getJsonDouble(json, "valorHoraExtra");
            int tempoProjetoMeses        = getJsonInt(json, "tempoProjetoMeses");
            double taxaServicoPercentual = getJsonDouble(json, "taxaServicoPercentual");

            if (valorContrato         > 0) f.setValorContrato(valorContrato);
            if (qtdHorasMensais       > 0) f.setQtdHorasMensais(qtdHorasMensais);
            if (valorHoraExtra        > 0) f.setValorHoraExtra(valorHoraExtra);
            if (tempoProjetoMeses     > 0) f.setTempoProjetoMeses(tempoProjetoMeses);
            if (taxaServicoPercentual > 0) f.setTaxaServicoPercentual(taxaServicoPercentual);

            return f;
        }
    }

    public static String toJsonFuncionario(FuncionarioBase f) {
        StringBuilder sb = new StringBuilder("{");

        sb.append("\"id\":").append(f.getId()).append(",");
        sb.append("\"nome\":\"").append(escape(f.getNome())).append("\",");
        sb.append("\"empresaId\":").append(f.getEmpresaId()).append(",");
        sb.append("\"tipo\":\"").append(f instanceof FuncionarioCLT ? "CLT" : "PJ").append("\",");
        sb.append("\"salario\":").append(f.calcularSalario());

        if (f instanceof FuncionarioCLT clt) {
            sb.append(",\"salarioFixo\":").append(clt.getSalarioFixo());
            sb.append(",\"beneficios\":").append(clt.getBeneficios());
            sb.append(",\"valeTransporte\":").append(clt.getValeTransporte());
            sb.append(",\"planoSaude\":").append(clt.getPlanoSaude());
            sb.append(",\"bonusAnual\":").append(clt.getBonusAnual());
        } else if (f instanceof FuncionarioPJ pj) {
            sb.append(",\"valorContrato\":").append(pj.getValorContrato());
            sb.append(",\"qtdHorasMensais\":").append(pj.getQtdHorasMensais());
            sb.append(",\"valorHoraExtra\":").append(pj.getValorHoraExtra());
            sb.append(",\"tempoProjetoMeses\":").append(pj.getTempoProjetoMeses());
            sb.append(",\"taxaServicoPercentual\":").append(pj.getTaxaServicoPercentual());
        }

        sb.append("}");
        return sb.toString();
    }

    public static String toJsonFuncionarios(List<FuncionarioBase> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (FuncionarioBase f : lista) {
            sb.append(toJsonFuncionario(f)).append(",");
        }
        if (!lista.isEmpty()) sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    // ========= HELPERS DE JSON =========

    public static String getJsonValue(String json, String key) {
        String find = "\"" + key + "\"";
        int i = json.indexOf(find);
        if (i == -1) return null;

        int s = json.indexOf(":", i) + 1;
        while (s < json.length() && Character.isWhitespace(json.charAt(s))) s++;

        if (s >= json.length()) return null;

        if (json.charAt(s) == '"') {
            int e = json.indexOf("\"", s + 1);
            if (e == -1) return null;
            return json.substring(s + 1, e);
        }

        int e = s;
        while (e < json.length() && json.charAt(e) != ',' && json.charAt(e) != '}')
            e++;

        return json.substring(s, e).trim();
    }

    public static int getJsonInt(String json, String key) {
        String v = getJsonValue(json, key);
        return v != null && !v.isBlank() ? Integer.parseInt(v) : 0;
    }

    public static double getJsonDouble(String json, String key) {
        String v = getJsonValue(json, key);
        return v != null && !v.isBlank() ? Double.parseDouble(v) : 0.0;
    }

    // ========= FUNÇÕES INTERNAS (SEM IMPORT DO JDK) =========

    private static String escape(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }
}