package com;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Server {

    public static void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        Router router = new Router();
        server.createContext("/", router::handle);
        server.setExecutor(null);

        server.start();
        System.out.println("Servidor iniciado em http://localhost:8080");
    }
}
