package org.academiadecodigo.bootcamp.service;

import org.academiadecodigo.bootcamp.model.User;
import org.academiadecodigo.bootcamp.persistence.ConnectionManager;
import org.academiadecodigo.bootcamp.utils.Security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class JdbcUserService implements UserService {

    private Connection dbConnection;
    private ConnectionManager connectionManager;

    public JdbcUserService(ConnectionManager connectionManager) {
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

    /**
     * Authenticates the user using the given username and password
     *
     * @param username the user name
     * @param password the user password
     * @return true if authenticated
     */
    @Override
    public boolean authenticate(String username, String password) {
        Statement statement = null;
        boolean result = false;

        try {

            checkConnection();

            statement = dbConnection.createStatement();

            // create a query
            String query = "SELECT * FROM user WHERE username='" + username + "' AND password='" + Security.getHash(password) + "';";

            // execute the query
            ResultSet resultSet = statement.executeQuery(query);

            // if the user exists
            if (resultSet.next()) {
                result = true;
            }

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
        } finally {
            closeConnection(statement);
        }

        return result;

    }

    /**
     * Adds a new user
     *
     * @param user the new user to add
     */
    @Override
    public void add(User user) {
        Statement statement = null;

        try {

            checkConnection();

            if (findByName(user.getUsername()) != null) {
                return;
            }


            // create a new statement
            statement = dbConnection.createStatement();

            // create a query
            String query = "INSERT INTO user(username, password, email, firstname, lastname, phone) VALUES ('" +
                    user.getUsername() + "','" +
                    user.getPassword() + "','" +
                    user.getEmail() + "','" +
                    user.getFirstName() + "','" +
                    user.getLastName() + "','" +
                    user.getPhone() + "');";

            // execute the query
            statement.executeUpdate(query);

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
        } finally {
            closeConnection(statement);
        }
    }

    /**
     * Finds a user by name
     *
     * @param username the user name used to lookup a user
     * @return a new User if found, null otherwise
     */
    @Override
    public User findByName(String username) {
        Statement statement = null;
        User user = null;

        try {

            checkConnection();

            // create a new statement
            statement = dbConnection.createStatement();

            // create a query
            String query = "SELECT * FROM user WHERE username='" + username + "'";

            // execute the query
            ResultSet resultSet = statement.executeQuery(query);

            // user exists
            if (resultSet.next()) {

                String usernameValue = resultSet.getString("username");
                String passwordValue = resultSet.getString("password");
                String emailValue = resultSet.getString("email");
                String firstNameValue = resultSet.getString("firstname");
                String lastNameValue = resultSet.getString("lastname");
                String phoneValue = resultSet.getString("phone");

                user = new User(usernameValue, emailValue, passwordValue, firstNameValue, lastNameValue, phoneValue);
            }

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
        } finally {
            closeConnection(statement);
        }

        return user;

    }

    /**
     * Finds all users
     *
     * @return list of users if found, null otherwise
     */
    @Override
    public List<User> findAll() {
        Statement statement = null;
        List<User> users = new LinkedList<>();

        try {

            checkConnection();

            // create a new statement
            statement = dbConnection.createStatement();

            // create a query
            String query = "SELECT * FROM user";

            // execute the query
            ResultSet resultSet = statement.executeQuery(query);

            // user exists
            while (resultSet.next()) {

                String usernameValue = resultSet.getString("username");
                String passwordValue = resultSet.getString("password");
                String emailValue = resultSet.getString("email");
                String firstNameValue = resultSet.getString("firstname");
                String lastNameValue = resultSet.getString("lastname");
                String phoneValue = resultSet.getString("phone");

                users.add(new User(usernameValue, emailValue, passwordValue, firstNameValue, lastNameValue, phoneValue));
            }

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
        } finally {
            closeConnection(statement);
        }

        return users;

    }

    /**
     * Count the number of existing users
     *
     * @return the number of users
     */
    @Override
    public int count() {
        Statement statement = null;
        int result = 0;

        try {

            checkConnection();

            // create a new statement
            statement = dbConnection.createStatement();

            // create a query
            String query = "SELECT COUNT(*) FROM user";

            // execute the query
            ResultSet resultSet = statement.executeQuery(query);

            // get the results
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println("Database error: " + ex.getMessage());
        } finally {
            closeConnection(statement);
        }

        return result;
    }

    private void closeConnection(Statement statement) {

        if (statement == null) {
            return;
        }

        try {
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Error releasing DB resources: " + ex.getMessage());
        }
    }
}
