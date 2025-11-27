package com.controllers;

import com.service.FuncionarioService;
import com.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import com.model.FuncionarioBase;
import com.model.FuncionarioCLT;
import com.model.FuncionarioPJ;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioController {

    private final FuncionarioService funcionarioService = new FuncionarioService();

    public void handle(HttpExchange exchange, String method, String path) throws IOException {
        try {

            if (path.equals("/funcionario") && method.equals("GET") && exchange.getRequestURI().getQuery() == null) {
                getAllFuncionarios(exchange);
                return;
            }


            if (path.equals("/funcionario") && method.equals("GET") && exchange.getRequestURI().getQuery() != null
                    && exchange.getRequestURI().getQuery().startsWith("empresaId=")) {

                String query = exchange.getRequestURI().getQuery();
                int empresaId = Integer.parseInt(query.split("=")[1]); // parse direto

                getFuncionariosPorEmpresa(exchange, empresaId);
                return;
            }


            if (path.matches("^/funcionario/\\d+$")) {
                int id = Integer.parseInt(path.split("/")[2]);
                switch (method) {
                    case "GET" -> getFuncionario(exchange, id);
                    case "PUT" -> updateFuncionario(exchange, id);
                    case "DELETE" -> deleteFuncionario(exchange, id);
                    default -> notAllowed(exchange);
                }
                return;
            }

            // ✅ POST /funcionario  → criar funcionario
            if (path.equals("/funcionario") && method.equals("POST")) {
                createFuncionario(exchange);
                return;
            }

            notFound(exchange);

        } catch (IllegalArgumentException e) {
            error(exchange, 400, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            error(exchange, 500, "Erro interno no servidor");
        }
    }

    private void createFuncionario(HttpExchange exchange) throws IOException, SQLException {
        String body = JsonUtil.readBody(exchange);

        String tipo = JsonUtil.getJsonValue(body, "tipo");
        String nome = JsonUtil.getJsonValue(body, "nome");
        int empresaId = JsonUtil.getJsonInt(body, "empresaId");

        if (tipo == null || nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("JSON inválido. Campos obrigatórios: tipo, nome, empresaId");
        }

        FuncionarioBase funcionario = "CLT".equalsIgnoreCase(tipo)
                ? new FuncionarioCLT(nome, empresaId)
                : new FuncionarioPJ(nome, empresaId);

        funcionarioService.create(funcionario);

        String response = "{\"msg\":\"Funcionario criado\",\"id\":" + funcionario.getId() + "}";
        JsonUtil.writeJson(exchange, 201, response);
    }

    private void getFuncionario(HttpExchange exchange, int id) throws IOException, SQLException {
        FuncionarioBase f = funcionarioService.findById(id);

        if (f == null) {
            JsonUtil.writeJson(exchange, 404, "{\"erro\":\"Funcionário não encontrado\"}");
            return;
        }

        JsonUtil.writeJson(exchange, 200, JsonUtil.toJsonFuncionario(f));
    }

    private void getFuncionariosPorEmpresa(HttpExchange exchange, int empresaId) throws IOException, SQLException {
        List<FuncionarioBase> list = funcionarioService.findAllByEmpresa(empresaId);

        // Monta um JSON válido
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(JsonUtil.toJsonFuncionario(list.get(i)));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");

        JsonUtil.writeJson(exchange, 200, sb.toString());
    }

    private void getAllFuncionarios(HttpExchange exchange) throws IOException, SQLException {
        List<FuncionarioBase> list = funcionarioService.findAll();

        // Mesma lógica para garantir JSON correto
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(JsonUtil.toJsonFuncionario(list.get(i)));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");

        JsonUtil.writeJson(exchange, 200, sb.toString());
    }


    private void getAllFuncionarios(HttpExchange exchange, int unused) throws IOException {}
    private void getFuncionariosPorEmpresa(HttpExchange ex, int emp, int unused) throws IOException {}

    private void updateFuncionario(HttpExchange exchange, int id) throws IOException, SQLException {
        String body = JsonUtil.readBody(exchange);
        String nome = JsonUtil.getJsonValue(body, "nome");

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Campo 'nome' é obrigatório no PUT");
        }

        FuncionarioBase existente = funcionarioService.findById(id);
        if (existente == null) {
            JsonUtil.writeJson(exchange, 404, "{\"erro\":\"Funcionário não encontrado\"}");
            return;
        }

        existente.setNome(nome);
        funcionarioService.update(id, existente);

        JsonUtil.writeJson(exchange, 200,
                "{\"msg\":\"Funcionário atualizado\",\"id\":" + id + ",\"nome\":\"" + nome + "\"}");
    }

    private void deleteFuncionario(HttpExchange exchange, int id) throws IOException, SQLException {
        boolean ok = funcionarioService.delete(id);
        if (!ok) {
            JsonUtil.writeJson(exchange, 404, "{\"erro\":\"Funcionário não encontrado para deletar\"}");
            return;
        }

        JsonUtil.writeJson(exchange, 200, "{\"msg\":\"Funcionário deletado\",\"id\":" + id + "}");
    }

    private void notFound(HttpExchange exchange) throws IOException {
        error(exchange, 404, "Rota não encontrada");
    }

    private void notAllowed(HttpExchange exchange) throws IOException {
        error(exchange, 405, "Método não permitido");
    }

    private void error(HttpExchange exchange, int status, String message) throws IOException {
        String json = "{\"erro\":\"" + message + "\"}";
        JsonUtil.writeJson(exchange, status, json);
    }
}
