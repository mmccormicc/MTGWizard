package org.capstone.mtgwizard;

import org.capstone.mtgwizard.domain.service.AllPricesDatabaseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GetPriceTest {

    AllPricesDatabaseHandler priceHandler;

    @BeforeEach
    void setup() {
        // Creating new all prices handler
        priceHandler = new AllPricesDatabaseHandler("src/main/resources/prices/AllPricesToday.json");

    }

    @Test
    void GetPrice_TCGPrice() {

        float cardPrice = priceHandler.getPrice("tcg", "Notfound");

        // Asserting equality
        assertEquals(-1f, cardPrice);
    }


}
