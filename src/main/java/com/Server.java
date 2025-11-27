package com;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Server {
    public static void start() throws Exception {
        HttpServer s = HttpServer.create(new InetSocketAddress(8080),0);
        Router r=new Router();
        s.createContext("/",r::handle);
        s.start();
        System.out.println("Servidor rodando na porta 8080");
    }
}
