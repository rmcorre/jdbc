package org.academiadecodigo.bootcamp.tests;

import org.academiadecodigo.bootcamp.model.User;
import org.academiadecodigo.bootcamp.persistence.ConnectionManager;
import org.academiadecodigo.bootcamp.service.JdbcUserService;
import org.academiadecodigo.bootcamp.utils.Security;

public class JdbcUserServiceTest {

    public static void main(String[] args) {

        ConnectionManager connectionManager = new ConnectionManager("jdbc:sqlite:C:\\Users\\rmcor\\Desktop\\postAcademiaDeCodigo\\jdbc\\src\\main\\resources\\test.db");
        JdbcUserService userService = new JdbcUserService(connectionManager);

        User user = new User(
                "dosAnjos",
                "dosAnjos@gmail.com",
                Security.getHash("dosAnjos123"),
                "Maria",
                "Pavao",
                "911234567"
        );

        userService.add(user);
        System.out.println(userService.authenticate("dosAnjos", "dosAnjos123"));

        User result;
        result = userService.findByName("dosAnjos");
        System.out.println(result.toString());

        System.out.println(userService.findAll());
        System.out.println(userService.count());
    }
}
