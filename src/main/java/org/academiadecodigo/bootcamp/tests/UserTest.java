package org.academiadecodigo.bootcamp.tests;

import org.academiadecodigo.bootcamp.model.User;

public class UserTest {

    public static void main(String[] args) {

        User user1 = new User(
                "fab",
                "fab@gmail.com",
                "fabme",
                "Fabio",
                "Barbosa",
                "910000000");

        System.out.println(user1.toString());
        System.out.println(user1.hashCode());
    }

}
