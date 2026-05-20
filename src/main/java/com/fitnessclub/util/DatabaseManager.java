package com.fitnessclub.util;

import com.fitnessclub.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {



    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=fitness_club;"
                    + "user=sa;"
                    + "password=Parola123!;"
                    + "encrypt=false;"
                    + "trustServerCertificate=true;";


    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        try {
            this.connection = DriverManager.getConnection(URL);
            System.out.println("Conexiune la SQL Server reușită.");
        } catch (SQLException e) {
            throw new DatabaseException("Nu s-a putut conecta: " + e.getMessage(), e);
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la reconectare: " + e.getMessage(), e);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexiunea a fost închisă.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la închidere: " + e.getMessage(), e);
        }
    }
}