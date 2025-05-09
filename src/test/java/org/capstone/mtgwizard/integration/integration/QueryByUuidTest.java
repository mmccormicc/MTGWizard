package org.capstone.mtgwizard.integration.integration;

import org.capstone.mtgwizard.domain.model.Card;
import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryByUuidTest {

    AllPrintingsDatabaseHandler testHandler;


    @BeforeEach
    void setup() {
        // All printings handler from local MySQL database for faster testing
        testHandler = new AllPrintingsDatabaseHandler("jdbc:mysql://localhost:3306/mtg", "mtguser", "password");
    }

    @Test
    void queryByUuid_ValidUuid_ReturnsCorrectCard() {

        // Standard UUID query
        String uuid = "3ef58d1c-993e-5ade-af6a-aa77edb53fd1";

        ArrayList<Card> resultCards = testHandler.queryByUuid(uuid);

        // Asserting correct number of cards is returned
        assertEquals(1, resultCards.size());

        // Checking name of first card returned
        Card firstCard = resultCards.get(0);
        assertEquals("Aragorn, King of Gondor", firstCard.getName());
        // Making sure uuid matches
        assertEquals("3ef58d1c-993e-5ade-af6a-aa77edb53fd1", firstCard.getUuid());
    }

    @Test
    void queryByUuid_InvalidUuid_ReturnsNoCards() {

        // Invalid UUID
        String uuid = "3ef58d1c-993e-5ade-0000-aa77edb53fd1";

        ArrayList<Card> resultCards = testHandler.queryByUuid(uuid);

        // Asserting no cards are returned
        assertEquals(0, resultCards.size());
    }

    @Test
    void queryByUuid_EmptyUuid_ReturnsNoCards() {

        // Invalid UUID
        String uuid = "";

        ArrayList<Card> resultCards = testHandler.queryByUuid(uuid);

        // Asserting no cards are returned
        assertEquals(0, resultCards.size());
    }

    @Test
    void queryByUuid_NullUuid_ReturnsNoCards() {

        // Invalid UUID
        String uuid = null;

        ArrayList<Card> resultCards = testHandler.queryByUuid(uuid);

        // Asserting no cards are returned
        assertEquals(0, resultCards.size());
    }

}
