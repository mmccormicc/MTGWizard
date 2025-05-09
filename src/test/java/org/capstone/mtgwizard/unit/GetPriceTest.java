package org.capstone.mtgwizard.unit;

import org.capstone.mtgwizard.domain.service.AllPricesDatabaseHandler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


public class GetPriceTest {

    AllPricesDatabaseHandler priceHandler;

    @BeforeEach
    void setup() {
        // Creating new all prices handler
        priceHandler = new AllPricesDatabaseHandler("src/main/resources/prices/AllPricesToday.json");

    }

    @Test
    void getPrice_NonexistentUUID_ReturnsMinusOne() {

        float cardPrice = priceHandler.getPrice("tcgplayer", "Notfound");

        // Asserting equality
        assertEquals(-1f, cardPrice);
    }

    @Test
    void getPrice_EmptySeller_ReturnsMinusOne() {

        float cardPrice = priceHandler.getPrice("", "3ef58d1c-993e-5ade-af6a-aa77edb53fd1");

        // Asserting equality
        assertEquals(-1f, cardPrice);
    }

    @Test
    void getPrice_TCGPrice_CorrectPrice() {

        float cardPrice = priceHandler.getPrice("tcgplayer", "3ef58d1c-993e-5ade-af6a-aa77edb53fd1");

        // Asserting equality
        assertEquals(0.69f, cardPrice);
    }

    @Test
    void getPrice_CardKingdomPrice_CorrectPrice() {

        float cardPrice = priceHandler.getPrice("cardkingdom", "3ef58d1c-993e-5ade-af6a-aa77edb53fd1");

        // Asserting equality
        assertEquals(1.49f, cardPrice);
    }



}
