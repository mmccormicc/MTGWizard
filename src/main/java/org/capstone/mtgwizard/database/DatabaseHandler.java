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
        String result = "";
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

        // Getting name preceded by name: string
        name = getCriteria(query, "name:");
        // If no name criteria is found, set name to whole text entry
        if(name.equals("")) {
            name = query;
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to MySQL database!");

            //Statement statement = connection.createStatement();

            // Querying cards table for card names that contain card name suplied by the user
            String selectQuery = "SELECT name, manaCost, text, setCode, type, uuid, isFullArt FROM cards WHERE LOWER(name) LIKE ?";

            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            preparedStatement.setString(1, "%" + name + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            int maxResults = 100;
            int numResults = 0;

            ArrayList<String[]> seenCards = new ArrayList<>();

            while (resultSet.next()) {

                System.out.println("name: " + resultSet.getString("name"));
                //    System.out.println("uuid: " + resultSet.getString("uuid"));
                //System.out.println("text: " + resultSet.getString("text"));
                //System.out.println("set: " + resultSet.getString("setCode"));
                //System.out.println("isFullArt: " + resultSet.getString("isFullArt"));
                //System.out.println("manaCost: " + resultSet.getString("manaCost"));
                //System.out.println();

                String[] seenCard = {resultSet.getString("name"), resultSet.getString("setCode")};

                boolean cardSeen = false;

                for (String[] cardIdentifier : seenCards) {
                    if (cardIdentifier[0].equals(seenCard[0]) && cardIdentifier[1].equals(seenCard[1])) {
                        cardSeen = true;
                    }
                }

                if (!cardSeen) {

                    String setName = getSetName(connection, resultSet.getString("setCode"));

                    cardList.add(createCard(resultSet.getString("name"), 0f, 0f, resultSet.getString("manaCost"),
                            resultSet.getString("text"), setName, resultSet.getString("type")));

                    numResults++;
                    if (numResults >= 100) {
                        break;
                    }

                    seenCards.add(seenCard);
                }

            }

            System.out.println(seenCards);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardList;

    }

    private String getSetName(Connection connection, String setCode) throws SQLException {
        System.out.println(setCode);
        // Querying cards table for card names that contain card name suplied by the user
        String setQuery = "SELECT name FROM sets WHERE code = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(setQuery);

        preparedStatement.setString(1, setCode);

        ResultSet resultSet = preparedStatement.executeQuery();

        String setName = "Not found.";

        while (resultSet.next()) {
            setName = resultSet.getString("name");
        }

        return setName;

    }

    // Recursive function which formats lines of card text to fit UI better by adding new lines
    private String formatString(String text) {
        // Vars used in while loop
        String fixedText = text;
        int charsSinceNewLine = 0;
        int textIndex = 0;

        // Continues until end of text is reached
        while(textIndex < fixedText.length()) {
            // If it has been 60 chars since the last new line
            if (charsSinceNewLine >= 60) {
                // Increasing index until end of word is found
                while (fixedText.charAt(textIndex) != ' ' && textIndex < fixedText.length() - 1) {
                    textIndex++;
                }
                // Inserting new line at index, then calling formatString for the rest of the text and appending it to end
                return fixedText.substring(0, textIndex) + "\n" + formatString(fixedText.substring(textIndex + 1, fixedText.length()));
            // If it hasn't been 60 chars since last new line
            } else {
                charsSinceNewLine++;
            }

            // Getting next char in text
            textIndex += 1;
            // This is done to prevent out of bounds errors
            // If end of text is reached
            if(textIndex == fixedText.length()) {
                return fixedText;
            // End of text not reached
            } else {
                // Checking if next two chars are a new line
                if (fixedText.substring(textIndex, textIndex + 1).equals("\n")) {
                    // Resetting charsSinceNewLine, as a new line has been found
                    charsSinceNewLine = 0;
                }
            }
        }
        return fixedText;
    }

    private Card createCard(String name, float cardKingdomPrice, float tcgPlayerPrice, String manaCost, String text, String setCode, String type) {

        // Removing curly braces so mana cost can be read by CardPanel class
        String fixedManaCost = manaCost;
        // If card doesn't have a mana cost
        if (fixedManaCost == null) {
            fixedManaCost = "";
        // Card does have a mana cost
        } else {
            fixedManaCost = fixedManaCost.replace("{", "");
            fixedManaCost = fixedManaCost.replace("}", "");
        }


        String fixedText = text;

        // If card doesn't have text
        if (fixedText == null) {
            fixedText = "";
        // Card does have text
        } else {
            // Replacing text to make it more readable
            fixedText = fixedText.replace("{T}", "(Tap)");
            fixedText = fixedText.replace("{W}", "{White}");
            fixedText = fixedText.replace("{U}", "{Blue}");
            fixedText = fixedText.replace("{B}", "{Black}");
            fixedText = fixedText.replace("{R}", "{Red}");
            fixedText = fixedText.replace("{G}", "{Green}");
        }

        // Formatting string text to add new lines
        fixedText = formatString(fixedText);


        return new Card(name, cardKingdomPrice, tcgPlayerPrice, fixedManaCost, fixedText, setCode, type);
    }

}
