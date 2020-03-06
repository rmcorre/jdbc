package org.academiadecodigo.bootcamp.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private Connection connection;
    private String dbUrl;

    public ConnectionManager(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public Connection getConnection() {

        try {

            if (connection == null) {
                connection = DriverManager.getConnection(dbUrl);
                System.out.println("Connection established");
            }

        } catch (SQLException ex) {
            System.out.println("Failure to connect to database : " + ex.getMessage());
        }

        return connection;
    }

    public void close() {

        try {

            if (connection != null) {
                connection.close();
                System.out.println("Connection closed");
            }

        } catch (SQLException ex) {
            System.out.println("Failure to close database connection: " + ex.getMessage());
        }
    }
}
