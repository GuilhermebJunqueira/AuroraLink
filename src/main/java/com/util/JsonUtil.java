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



    public static Empresa fromJsonEmpresa(String json) {
        Empresa e = new Empresa();
        e.setNome(getJsonValue(json, "nome"));
        e.setCnpj(getJsonValue(json, "cnpj"));
        return e;
    }

    public static String toJson(Empresa e) {
        return "{"
                + "\"id\":" + e.getId() + ","
                + "\"nome\":\"" + e.getNome() + "\","
                + "\"cnpj\":\"" + e.getCnpj() + "\""
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



    public static FuncionarioBase fromJsonFuncionario(String json) {
        String tipo = getJsonValue(json, "tipo");
        String nome = getJsonValue(json, "nome");
        int empresaId = getJsonInt(json, "empresaId");

        return "CLT".equalsIgnoreCase(tipo)
                ? new FuncionarioCLT(nome, empresaId)
                : new FuncionarioPJ(nome, empresaId);
    }

    public static String toJsonFuncionario(FuncionarioBase f) {
        String tipo = (f instanceof FuncionarioCLT) ? "CLT" : "PJ";

        return "{"
                + "\"id\":" + f.getId() + ","
                + "\"nome\":\"" + f.getNome() + "\","
                + "\"empresaId\":" + f.getEmpresaId() + ","
                + "\"tipo\":\"" + tipo + "\","
                + "\"salario\":" + f.calcularSalario()
                + "}";
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


    public static String getJsonValue(String json, String key) {
        String find = "\"" + key + "\"";
        int i = json.indexOf(find);
        if (i == -1) return null;

        int s = json.indexOf(":", i) + 1;
        while (Character.isWhitespace(json.charAt(s))) s++;

        if (json.charAt(s) == '"') {
            int e = json.indexOf("\"", s + 1);
            return json.substring(s + 1, e);
        }

        int e = s;
        while (e < json.length() && json.charAt(e) != ',' && json.charAt(e) != '}')
            e++;

        return json.substring(s, e).trim();
    }

    public static int getJsonInt(String json, String key) {
        String v = getJsonValue(json, key);
        return v != null ? Integer.parseInt(v) : 0;
    }

    public static double getJsonDouble(String json, String key) {
        String v = getJsonValue(json, key);
        return v != null ? Double.parseDouble(v) : 0.0;
    }
}