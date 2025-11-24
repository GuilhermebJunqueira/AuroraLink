package com;

import com.controllers.EmpresaController;
import com.controllers.FuncionarioController;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class Router {

    private final EmpresaController empresaController = new EmpresaController();
    private final FuncionarioController funcionarioController = new FuncionarioController();

    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.startsWith("/empresa")) {
            empresaController.handle(exchange, method, path);
            return;
        }

        if (path.startsWith("/funcionario")) {
            funcionarioController.handle(exchange, method, path);
            return;
        }

        String msg = "Rota não encontrada: " + path;
        exchange.sendResponseHeaders(404, msg.getBytes().length);
        exchange.getResponseBody().write(msg.getBytes());
        exchange.getResponseBody().close();
    }
}
