package org.capstone.mtgwizard.integration;

import org.capstone.mtgwizard.domain.model.Card;
import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryDatabaseTest {

    AllPrintingsDatabaseHandler testHandler;
    String[] tags;


    @BeforeEach
    void setup() {
        // All printings handler from local MySQL database for faster testing
        testHandler = new AllPrintingsDatabaseHandler("jdbc:mysql://localhost:3306/mtg", "mtguser", "password");
    }

    @Test
    void queryDatabase_NameOnlyQuery_ReturnsCorrectCards() {

        // Standard name query
        String query = "sarkhan";

        ArrayList<Card> resultCards = testHandler.queryDatabase(query);

        // Asserting correct number of cards is returned
        assertEquals(43, resultCards.size());

        // Checking name of first card returned
        Card firstCard = resultCards.get(0);
        assertEquals("Sarkhan Vol", firstCard.getName());
    }

    @Test
    void queryDatabase_NameAndSetQuery_ReturnsOneCard() {

        // Name and set query
        String query = "name: Inspiring Overseer set:SNC";

        ArrayList<Card> resultCards = testHandler.queryDatabase(query);

        // Asserting correct number of cards is returned
        assertEquals(1, resultCards.size());

        Card firstCard = resultCards.get(0);
        assertEquals("Inspiring Overseer", firstCard.getName());
    }

    @Test
    void queryDatabase_SetOnlyQuery_ReturnsMaxCards() {

        // Set only query
        String query = "set:SNC";

        ArrayList<Card> resultCards = testHandler.queryDatabase(query);

        // Asserting correct number of cards is returned
        assertEquals(100, resultCards.size());

        Card firstCard = resultCards.get(0);
        assertEquals("Angelic Observer", firstCard.getName());
    }

    @Test
    void queryDatabase_EmptyQuery_ReturnsNoCards() {
        String query = "";

        ArrayList<Card> resultCards = testHandler.queryDatabase(query);

        // Asserting no cards are returned
        assertEquals(0, resultCards.size());
    }

    @Test
    void queryDatabase_NullQuery_ReturnsNoCards() {
        String query = null;

        ArrayList<Card> resultCards = testHandler.queryDatabase(query);

        // Asserting no cards are returned
        assertEquals(0, resultCards.size());
    }

    @Test
    void queryDatabase_EmptyNameAndSet_ReturnsNoCards() {
        String query = "name:set:";

        ArrayList<Card> resultCards = testHandler.queryDatabase(query);

        // Asserting no cards are returned
        assertEquals(0, resultCards.size());
    }

    @Test
    void queryDatabase_NameAndSetQueryOnlineDatabase_ReturnsCorrectCards() {
        // Connecting to online database
        testHandler =  new AllPrintingsDatabaseHandler(
                        "jdbc:mysql://gondola.proxy.rlwy.net:39906/mtg",
                        "readonly",
                        "readonly_password"
                );

        // Name and set query
        String query = "name: Inspiring Overseer set:SNC";

        ArrayList<Card> resultCards = testHandler.queryDatabase(query);

        // Asserting correct number of cards is returned
        assertEquals(1, resultCards.size());

        Card firstCard = resultCards.get(0);
        assertEquals("Inspiring Overseer", firstCard.getName());

    }

}
