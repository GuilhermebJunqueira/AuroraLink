package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:aurorlalink.db";

    static {
        init();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private static void init() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS empresa (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    cnpj TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS funcionario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    salario REAL NOT NULL,
                    tipo TEXT NOT NULL,
                    empresa_id INTEGER NOT NULL,
                    FOREIGN KEY (empresa_id) REFERENCES empresa(id)
                );
            """);

            System.out.println("Banco pronto!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
