package org.capstone.mtgwizard.database;

import org.capstone.mtgwizard.dataobjects.Card;

import org.capstone.mtgwizard.database.AllPricesDatabaseHandler;

import java.sql.*;
import java.util.ArrayList;

public class AllPrintingsDatabaseHandler {

    private String url;
    private String username;
    private String password;

    public AllPrintingsDatabaseHandler(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public ArrayList<Card> queryDatabase(String query) {

        // Holds list of cards returned by query
        ArrayList<Card> cardList = new ArrayList<>();

        // Pulled from query string, used to query cards
        String name;
        String set;

        // Setting query to lower case so search isn't case-sensitive
        query = query.toLowerCase();

        // Getting name preceded by "name"
        name = getCriteria(query, "name:");
        // If no name criteria is found, set name to whole text entry
        if(name.equals("")) {
            name = query;
        }

        // Getting set preceded by "set:"
        set = getCriteria(query, "set:");

        System.out.println(set);

        // Code below uses connection to MySQL
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Confirmation message
            System.out.println("Connected to MySQL database!");


            // Querying cards table for card names that contain card name string supplied by the user
            String selectQuery = "SELECT name, manaCost, text, setCode, type, uuid, isFullArt FROM cards WHERE LOWER(name) LIKE ?";

            // Creating prepared statement from query
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            // Inserting name into prepared statement
            preparedStatement.setString(1, "%" + name + "%");

            // Executing query and getting result set
            ResultSet resultSet = preparedStatement.executeQuery();


            // Max number of results to show
            int maxResults = 100;
            // Number of results that will be shown
            int numResults = 0;

            // Arraylist containing info of cards already seen in result set
            ArrayList<String[]> seenCards = new ArrayList<>();

            // Looping through entire result set
            while (resultSet.next()) {

                // Printing out found card info
                System.out.println("name: " + resultSet.getString("name"));
                //    System.out.println("uuid: " + resultSet.getString("uuid"));
                //System.out.println("text: " + resultSet.getString("text"));
                //System.out.println("set: " + resultSet.getString("setCode"));
                //System.out.println("isFullArt: " + resultSet.getString("isFullArt"));
                //System.out.println("manaCost: " + resultSet.getString("manaCost"));
                //System.out.println();

                // Creating array representing seen card
                String[] seenCard = {resultSet.getString("name"), resultSet.getString("setCode")};
                // Boolean if card already found
                boolean cardSeen = false;

                // Comparing seen card against previously seen cards
                for (String[] cardIdentifier : seenCards) {
                    // If name and set match
                    if (cardIdentifier[0].equals(seenCard[0]) && cardIdentifier[1].equals(seenCard[1])) {
                        // Card was seen
                        cardSeen = true;
                    }
                }
                // If card hasn't been seen yet
                if (!cardSeen) {

                    // Converting set code into set name
                    String setName = getSetName(connection, resultSet.getString("setCode"));

                    // Getting prices
                    //Float[] prices = pricesHandler.getPrices();

                    // Adding card to card results
                    cardList.add(createCard(resultSet.getString("name"), -1f, -1f, resultSet.getString("manaCost"),
                            resultSet.getString("text"), setName, resultSet.getString("type"), resultSet.getString("uuid")));

                    // Increasing number of results found
                    numResults++;

                    // If found more or the same number of cards as max that can be displayed
                    if (numResults >= maxResults) {
                        // Breaking out of result set loop
                        break;
                    }

                    // Adding seen card to seen cards
                    seenCards.add(seenCard);
                }

            }

        // SQL exception if query or connection produces an error
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardList;
    }

    // Pulls a string from the query if it is preceded by criteria word such as "name:"
    private String getCriteria(String query, String criteria) {
        // Holds result string
        String result = "";

        // Starting index of query
        int nameIndex = query.indexOf(criteria);
        // If criteria was found in string
        if (nameIndex != -1) {
            // Setting index to not include search criteria
            int nameStartIndex = nameIndex + criteria.length();
            // If there is a space preceding target string, go past it
            if (query.charAt(nameStartIndex) == ' ') {
                nameStartIndex++;
            }

            // Initializing end index at start index
            int nameEndIndex = nameStartIndex;
            // Looping until end of search query
            while (nameEndIndex < query.length()) {
                nameEndIndex++;
            }

            // Setting result string from criteria
            result = query.substring(nameStartIndex, nameEndIndex);
        }

        return result;
    }

    private String getSetName(Connection connection, String setCode) throws SQLException {
        // SQL query for getting set name from set code
        String setQuery = "SELECT name FROM sets WHERE code = ?";

        // Creating prepared statement from query
        PreparedStatement preparedStatement = connection.prepareStatement(setQuery);
        // Inserting set code into prepared statement
        preparedStatement.setString(1, setCode);

        // Executing query and getting result set of set names
        ResultSet resultSet = preparedStatement.executeQuery();

        // Initializing set name as not found in case set code doesn't match any set names
        String setName = "Not found.";

        // Looping through result set. Should be only 1 set name that matches set code.
        while (resultSet.next()) {
            // Setting set name to be found name with set code in parentheses
            setName = resultSet.getString("name") + " (" + setCode + ")";
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

    private Card createCard(String name, float cardKingdomPrice, float tcgPlayerPrice, String manaCost, String text, String setCode, String type, String uuid) {

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


        return new Card(name, cardKingdomPrice, tcgPlayerPrice, fixedManaCost, fixedText, setCode, type, uuid);
    }

}
