package org.capstone.mtgwizard.database;

import java.sql.*;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mtg"; // Replace with your database URL
        String username = "root"; // Replace with your MySQL username
        String password = "Lucca181630!"; // Replace with your MySQL password

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Your database operations here
            System.out.println("Connected to MySQL database!");

            Statement statement = connection.createStatement();

            String selectQuery = "SELECT name, uuid, text FROM cards";
            ResultSet resultSet = statement.executeQuery(selectQuery);


            for (int n = 0; n < 25; n++) {
                resultSet.next();
                System.out.println("name: " + resultSet.getString("name"));
                System.out.println("uuid: " + resultSet.getString("uuid"));
                System.out.println("text: " + resultSet.getString("text"));
                System.out.println();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
