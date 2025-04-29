package org.capstone.mtgwizard.domain.service;

import org.capstone.mtgwizard.domain.model.Card;

import java.sql.*;
import java.util.ArrayList;

public class AllPrintingsDatabaseHandler {

    private String dbURL;
    private String dbUsername;
    private String dbPassword;

    private Connection connection = null;

    public AllPrintingsDatabaseHandler(String url, String username, String password) {
        this.dbURL = url;
        this.dbUsername = username;
        this.dbPassword = password;

        try {
            connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            System.out.println("Successful SQL Connection.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Card> queryDatabase(String query) {

        // Holds list of cards returned by query
        ArrayList<Card> cardList = new ArrayList<>();

        // Setting query to lower case so search isn't case-sensitive
        query = query.toLowerCase();

        // Holds search criteria found
        String[] criteria;

        // Holds name and set individually
        String name = "";
        String set = "";

        // Tags that are searched for in query
        String[] tags = {"name:", "set:"};

        // Getting criteria based on tags
        criteria = getCriteria(query, tags);

        // If no name or set criteria is found, set name to whole text entry
        if (criteria[0].equals("") && criteria[1].equals("")) {
            name = query;
        } else {
            // If a criteria is found, set both to resulting criteria
            name = criteria[0];
            set = criteria[1];
        }

        if (connection != null) {
            // Code below uses connection to MySQL
            try {

                PreparedStatement preparedStatement = createSqlStatement(connection, name, set);

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
        }
        return cardList;
    }

    // Pulls a string from the query if it is preceded by criteria word such as "name:"
    public String[] getCriteria(String query, String[] tags) {

        // Holds result array of found criteria
        String[] criteriaArray = {"", ""};

        // Holds tag starting indexes
        ArrayList<Integer> tagIndexes = new ArrayList<>();

        // Getting starting index of each supplied tag
        for (String tag : tags) {
            tagIndexes.add(query.indexOf(tag));
        }

        // For each tag
        for (int n = 0; n < tags.length; n++) {
            // Getting tag
            String tag = tags[n];
            // Getting start index of tag
            int tagIndex = tagIndexes.get(n);

            if (tagIndex != -1) {
                // Offsetting start index by length of tag name
                int criteriaStartIndex = tagIndex + tag.length();

                // If there is a space preceding target criteria, go past it
                if (criteriaStartIndex < query.length() && query.charAt(criteriaStartIndex) == ' ') {
                    criteriaStartIndex++;
                }

                // Initializing end index at start index
                int criteriaEndIndex = criteriaStartIndex;
                // Looping until end of search query, or another tag index is reached
                while (criteriaEndIndex < query.length()) {
                    if (tagIndexes.contains(criteriaEndIndex)) {
                        break;
                    }
                    criteriaEndIndex++;
                }


                // Creating criteria string using index bounds
                String criteria = query.substring(criteriaStartIndex, criteriaEndIndex);
                // Removing space from end of criteria if it has one
                if (criteria.endsWith(" ")) {
                    criteria = criteria.substring(0, criteria.length() - 1);
                }

                // Setting criteria in array to be criteria
                criteriaArray[n] = criteria;
                //System.out.println("Criteria found: " + result[n]);
            }
        }

        return criteriaArray;

    }

    private PreparedStatement createSqlStatement(Connection connection, String name, String set) throws SQLException {

        PreparedStatement preparedStatement = null;

        // Query has a name but no set
        if (!name.equals("") && set.equals("")) {
            // Querying cards table for card names that contain card name string supplied by the user
            String selectQuery = "SELECT name, manaCost, text, setCode, type, uuid, isFullArt FROM cards WHERE LOWER(name) LIKE ?";

            // Creating prepared statement from query
            preparedStatement = connection.prepareStatement(selectQuery);
            // Inserting name into prepared statement
            preparedStatement.setString(1, "%" + name + "%");

            // Query has a set but no name
        } else if (name.equals("") && !set.equals("")) {
            // Querying cards table for cards that have given set code
            String selectQuery = "SELECT name, manaCost, text, setCode, type, uuid, isFullArt FROM cards WHERE LOWER(setCode) = ?";

            // Creating prepared statement from query
            preparedStatement = connection.prepareStatement(selectQuery);
            // Inserting set code into prepared statement
            preparedStatement.setString(1, set);

            // Query has a set and name
        } else {
            System.out.println("BOTH FOUND");
            // Querying cards table for card names that contain card name string supplied by the user, and match given set code
            String selectQuery = "SELECT name, manaCost, text, setCode, type, uuid, isFullArt FROM cards WHERE LOWER(setCode) = ? AND LOWER(name) LIKE ?";

            // Creating prepared statement from query
            preparedStatement = connection.prepareStatement(selectQuery);
            // Inserting set code into prepared statement
            preparedStatement.setString(1, set);
            // Inserting name into prepared statement
            preparedStatement.setString(2, "%" + name + "%");
        }

        return preparedStatement;
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
    private String formatText(String text) {
        // Vars used in while loop
        String fixedText = text;
        int charsSinceNewLine = 0;
        int textIndex = 0;

        // Continues until end of text is reached
        while (textIndex < fixedText.length()) {
            // If it has been 60 chars since the last new line
            if (charsSinceNewLine >= 60) {
                // Increasing index until end of word is found
                while (fixedText.charAt(textIndex) != ' ' && textIndex < fixedText.length() - 1) {
                    textIndex++;
                }
                // Inserting new line at index, then calling formatString for the rest of the text and appending it to end
                return fixedText.substring(0, textIndex) + "\n" + formatText(fixedText.substring(textIndex + 1, fixedText.length()));
                // If it hasn't been 60 chars since last new line
            } else {
                charsSinceNewLine++;
            }

            // Getting next char in text
            textIndex += 1;
            // This is done to prevent out of bounds errors
            // If end of text is reached
            if (textIndex == fixedText.length()) {
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

    public ArrayList<Card> queryByUuid(String uuid) {

        ArrayList<Card> cardList = new ArrayList();

        // Code below uses connection to MySQL
        if (connection != null) {
            try {

                // Looking for first entry where uuid matches
                String selectQuery = "SELECT name, manaCost, text, setCode, type, uuid FROM cards WHERE uuid = ? LIMIT 1";

                // Creating prepared statement from query
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                // Inserting set code into prepared statement
                preparedStatement.setString(1, uuid);

                // Executing query and getting result set
                ResultSet resultSet = preparedStatement.executeQuery();


                // Looping through entire result set
                while (resultSet.next()) {

                    // Converting set code into set name
                    String setName = getSetName(connection, resultSet.getString("setCode"));

                    // Adding card to card results
                    cardList.add(createCard(resultSet.getString("name"), -1f, -1f, resultSet.getString("manaCost"),
                            resultSet.getString("text"), setName, resultSet.getString("type"), resultSet.getString("uuid")));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return cardList;
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
//            fixedText = fixedText.replace("{W}", "{White}");
//            fixedText = fixedText.replace("{U}", "{Blue}");
//            fixedText = fixedText.replace("{B}", "{Black}");
//            fixedText = fixedText.replace("{R}", "{Red}");
//            fixedText = fixedText.replace("{G}", "{Green}");
        }

        // Formatting string text to add new lines
        fixedText = formatText(fixedText);


        return new Card(name, cardKingdomPrice, tcgPlayerPrice, fixedManaCost, fixedText, setCode, type, uuid);
    }

}

