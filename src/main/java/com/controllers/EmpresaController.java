package com.controllers;

import com.model.Empresa;
import com.service.EmpresaService;
import com.service.FuncionarioService;
import com.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.SQLException;

public class EmpresaController {

    private final EmpresaService empresaService = new EmpresaService();
    private final FuncionarioService funcionarioService = new FuncionarioService();

    public void handle(HttpExchange exchange, String method, String path) throws IOException {
        try {
            if (path.equals("/empresa")) {
                switch (method) {
                    case "GET" -> listEmpresas(exchange);
                    case "POST" -> createEmpresa(exchange);
                    default -> notAllowed(exchange);
                }
                return;
            }

            // /empresa/{id}
            if (path.matches("^/empresa/\\d+$")) {
                int id = Integer.parseInt(path.split("/")[2]);
                switch (method) {
                    case "GET" -> getEmpresa(exchange, id);
                    case "PUT" -> updateEmpresa(exchange, id);
                    case "DELETE" -> deleteEmpresa(exchange, id);
                    default -> notAllowed(exchange);
                }
                return;
            }

            // /empresa/{id}/funcionarios
            if (path.matches("^/empresa/\\d+/funcionarios$")) {
                int idEmpresa = Integer.parseInt(path.split("/")[2]);
                if (method.equals("GET")) {
                    listFuncionariosByEmpresa(exchange, idEmpresa);
                } else {
                    notAllowed(exchange);
                }
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

    private void listEmpresas(HttpExchange exchange) throws IOException, SQLException {
        var empresas = empresaService.findAll();
        String json = JsonUtil.toJsonEmpresas(empresas);
        JsonUtil.writeJson(exchange, 200, json);
    }

    private void createEmpresa(HttpExchange exchange) throws IOException, SQLException {
        String body = JsonUtil.readBody(exchange);
        Empresa empresa = JsonUtil.fromJsonEmpresa(body);
        empresaService.create(empresa);
        JsonUtil.writeJson(exchange, 201, JsonUtil.toJson(empresa));
    }

    private void getEmpresa(HttpExchange exchange, int id) throws IOException, SQLException {
        Empresa empresa = empresaService.findById(id);

        if (empresa == null) {
            error(exchange, 404, "Empresa com ID " + id + " não existe");
            return;
        }

        JsonUtil.writeJson(exchange, 200, JsonUtil.toJson(empresa));
    }


    private void updateEmpresa(HttpExchange exchange, int id) throws IOException, SQLException {
        String body = JsonUtil.readBody(exchange);
        Empresa empresa = JsonUtil.fromJsonEmpresa(body);
        empresaService.update(id, empresa);
        JsonUtil.writeJson(exchange, 204, "");
    }

    private void deleteEmpresa(HttpExchange exchange, int id) throws IOException, SQLException {
        Empresa empresa = empresaService.findById(id);

        if (empresa == null) {
            error(exchange, 404, "Empresa com ID " + id + " não existe para deletar");
            return;
        }

        empresaService.delete(id);

        String msg = "{\"mensagem\":\"Empresa deletada com sucesso\",\"id\":" + id + "}";
        JsonUtil.writeJson(exchange, 200, msg);
    }



    private void listFuncionariosByEmpresa(HttpExchange exchange, int idEmpresa) throws IOException, SQLException {
        var funcionarios = funcionarioService.findAllByEmpresa(idEmpresa);
        String json = JsonUtil.toJsonFuncionarios(funcionarios);
        JsonUtil.writeJson(exchange, 200, json);
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
