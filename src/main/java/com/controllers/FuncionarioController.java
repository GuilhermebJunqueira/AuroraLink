package com.controllers;

import com.model.Funcionario;
import com.service.FuncionarioService;
import com.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.SQLException;

public class FuncionarioController {

    private final FuncionarioService funcionarioService = new FuncionarioService();

    public void handle(HttpExchange exchange, String method, String path) throws IOException {
        try {
            // GET /funcionario/{id}
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

            // POST /funcionario
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
        Funcionario funcionario = JsonUtil.fromJsonFuncionario(body);
        funcionarioService.create(funcionario);
        JsonUtil.writeJson(exchange, 201, JsonUtil.toJson(funcionario));
    }

    private void getFuncionario(HttpExchange exchange, int id) throws IOException, SQLException {
        Funcionario funcionario = funcionarioService.findById(id);

        if (funcionario == null) {
            error(exchange, 404, "Funcionário com ID " + id + " não existe");
            return;
        }

        JsonUtil.writeJson(exchange, 200, JsonUtil.toJson(funcionario));
    }


    private void updateFuncionario(HttpExchange exchange, int id) throws IOException, SQLException {
        String body = JsonUtil.readBody(exchange);
        Funcionario funcionario = JsonUtil.fromJsonFuncionario(body);
        funcionarioService.update(id, funcionario);
        JsonUtil.writeJson(exchange, 204, "");
    }

    private void deleteFuncionario(HttpExchange exchange, int id) throws IOException, SQLException {
        funcionarioService.delete(id);

        String resposta = "{ \"mensagem\": \"Funcionário deletado com sucesso\", \"id\": " + id + " }";
        JsonUtil.writeJson(exchange, 200, resposta);
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
