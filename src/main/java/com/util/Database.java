package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:auroralink.db";

    static {
        init();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private static void init() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Tabela Empresa
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS empresa (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    cnpj TEXT NOT NULL
                );
            """);

            // Tabela Funcionario
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

            System.out.println("Banco inicializado com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
