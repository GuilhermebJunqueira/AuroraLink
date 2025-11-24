package com.util;

import com.model.Empresa;
import com.model.Funcionario;
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

    public static void writeJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }

    // ---------------- EMPRESA ----------------

    public static Empresa fromJsonEmpresa(String json) {
        Empresa e = new Empresa();
        e.setNome(extract(json, "nome"));
        e.setCnpj(extract(json, "cnpj"));
        return e;
    }

    public static String toJson(Empresa e) {
        return "{"
                + "\"id\":" + e.getId() + ","
                + "\"nome\":\"" + escape(e.getNome()) + "\","
                + "\"cnpj\":\"" + escape(e.getCnpj()) + "\""
                + "}";
    }

    public static String toJsonEmpresas(List<Empresa> empresas) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < empresas.size(); i++) {
            sb.append(toJson(empresas.get(i)));
            if (i < empresas.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    // ---------------- FUNCIONARIO ----------------

    public static Funcionario fromJsonFuncionario(String json) {
        Funcionario f = new Funcionario();
        f.setNome(extract(json, "nome"));
        String tipo = extract(json, "tipo");
        f.setTipo(tipo);

        String empresaIdStr = extract(json, "empresaId");
        if (empresaIdStr != null && !empresaIdStr.isBlank()) {
            f.setEmpresaId(Integer.parseInt(empresaIdStr));
        }
        // salario será calculado no service via polimorfismo
        return f;
    }

    public static String toJson(Funcionario f) {
        return "{"
                + "\"id\":" + f.getId() + ","
                + "\"nome\":\"" + escape(f.getNome()) + "\","
                + "\"salario\":" + f.getSalario() + ","
                + "\"tipo\":\"" + escape(f.getTipo()) + "\","
                + "\"empresaId\":" + f.getEmpresaId()
                + "}";
    }

    public static String toJsonFuncionarios(List<Funcionario> funcionarios) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < funcionarios.size(); i++) {
            sb.append(toJson(funcionarios.get(i)));
            if (i < funcionarios.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    // ------------- helpers simples -------------

    private static String extract(String json, String field) {
        // MUITO simples, só pra este trabalho – assume estrutura: "field": "valor" ou "field": 123
        String key = "\"" + field + "\"";
        int idx = json.indexOf(key);
        if (idx == -1) return null;
        int colon = json.indexOf(":", idx);
        if (colon == -1) return null;
        int start = colon + 1;

        // pula espaços
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        char first = json.charAt(start);
        if (first == '\"') {
            int end = json.indexOf("\"", start + 1);
            if (end == -1) return null;
            return json.substring(start + 1, end);
        } else {
            int end = start;
            while (end < json.length()
                    && !Character.isWhitespace(json.charAt(end))
                    && json.charAt(end) != ','
                    && json.charAt(end) != '}') {
                end++;
            }
            return json.substring(start, end);
        }
    }

    private static String escape(String value) {
        if (value == null) return "";
        return value.replace("\"", "\\\"");
    }
}
