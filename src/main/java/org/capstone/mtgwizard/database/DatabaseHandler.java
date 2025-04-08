package org.capstone.mtgwizard.database;

import org.capstone.mtgwizard.dataobjects.Card;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {

    private String url;
    private String username;
    private String password;

    public DatabaseHandler(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private String getCriteria(String query, String criteria) {
        String result = "Not found";
        // System.out.println(query);
        int nameIndex = query.indexOf(criteria);
        if (nameIndex != -1) {
            int nameStartIndex = nameIndex + 5;
            if (query.charAt(nameStartIndex) == ' ') {
                nameStartIndex++;
            }
            int nameEndIndex = nameStartIndex;
            while (nameEndIndex < query.length()) {
                nameEndIndex++;
                //System.out.println(nameEndIndex);
            }
            result = query.substring(nameStartIndex, nameEndIndex);
        }
        System.out.println(result);
        return result;
    }

    public ArrayList<Card> queryDatabase(String query) {
        ArrayList<Card> cardList = new ArrayList<>();

        String name;
        String set;
        String uuid;

        query = query.toLowerCase();

        name = getCriteria(query, "name:");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to MySQL database!");

            //Statement statement = connection.createStatement();

            String selectQuery = "SELECT name, manaCost, text, setCode, type, uuid, isFullArt FROM cards WHERE LOWER(name) LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setString(1, "%" + name + "%");

            ResultSet resultSet = preparedStatement.executeQuery();


            for (int n = 0; n < 10; n++) {
                resultSet.next();
                System.out.println("name: " + resultSet.getString("name"));
                //    System.out.println("uuid: " + resultSet.getString("uuid"));
                //System.out.println("text: " + resultSet.getString("text"));
                System.out.println("set: " + resultSet.getString("setCode"));
                System.out.println("isFullArt: " + resultSet.getString("isFullArt"));
                System.out.println();
                cardList.add(new Card(resultSet.getString("name"), 0f, 0f, resultSet.getString("manaCost"),
                        resultSet.getString("text"), resultSet.getString("setCode"), resultSet.getString("type")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardList;

    }

}
