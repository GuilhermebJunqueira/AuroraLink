package com;

import com.sun.net.httpserver.HttpExchange;
import com.controllers.FuncionarioController;
import com.controllers.EmpresaController;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Router {

    private final FuncionarioController funcionarioController = new FuncionarioController();
    private final EmpresaController empresaController = new EmpresaController();

    public void handle(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        String method = ex.getRequestMethod();

        // Cabeçalho padrão
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

        try {
            if (path.startsWith("/empresa")) {
                // Chamada correta: exchange, method, path
                empresaController.handle(ex, method, path);
                return;
            }

            if (path.startsWith("/funcionario")) {
                funcionarioController.handle(ex, method, path);
                return;
            }

            // Rota inexistente
            send(ex, 404, "{\"erro\":\"Rota não encontrada\"}");

        } catch (Exception e) {
            // Em caso de erro interno
            String msg = "{\"erro\":\"" + escapeForJson(e.getMessage()) + "\"}";
            send(ex, 500, msg);
        }
    }

    private void send(HttpExchange ex, int code, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }

    // evita quebra do JSON se a mensagem tiver aspas
    private String escapeForJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
