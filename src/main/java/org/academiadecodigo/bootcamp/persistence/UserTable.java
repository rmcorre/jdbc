package org.academiadecodigo.bootcamp.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTable {

    ConnectionManager connectionManager;
    Connection dbConnection;

    public UserTable(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.dbConnection = connectionManager.getConnection();
    }

    private void checkConnection() throws SQLException {

        if (dbConnection == null || dbConnection.isClosed()) {
            dbConnection = connectionManager.getConnection();
        }

        if (dbConnection == null) {
            throw new IllegalStateException("Error connecting to the database");
        }
    }

    public void create() {

        Statement statement = null;

        try {

            checkConnection();

            statement = dbConnection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS user (\n"
                    + "    id INTEGER PRIMARY KEY,\n"
                    + "    username TEXT NOT NULL,\n"
                    + "    password TEXT NOT NULL,\n"
                    + "    email TEXT NOT NULL,\n"
                    + "    firstname TEXT NOT NULL,\n"
                    + "    lastname TEXT NOT NULL,\n"
                    + "    phone TEXT NOT NULL\n"
                    + ");";

            statement.execute(sql);
            System.out.println("User table created");

        } catch (SQLException e) {
            System.out.println("User table creation error: " + e.getMessage());
        } finally {
            closeConnection(statement);
            System.out.println("Connection closed");
        }
    }

    private void closeConnection(Statement statement) {

        if (statement == null) {
            return;
        }

        try {
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Error releasing database resources: " + ex.getMessage());
        }
    }


}
