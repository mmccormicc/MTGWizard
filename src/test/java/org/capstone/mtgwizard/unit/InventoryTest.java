package org.capstone.mtgwizard.unit;

import org.capstone.mtgwizard.domain.model.Card;
import org.capstone.mtgwizard.domain.model.Inventory;
import org.capstone.mtgwizard.domain.model.Inventory.RemoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inventory;
    private Card sampleCard;

    @BeforeEach
    void setUp() {
        // Inventory for testing
        inventory = new Inventory();
        // Card for testing
        sampleCard = new Card("Sample Card",
                0f,
                0f,
                "G",
                "Sample Rules",
                "TST",
                "Creature",
                "test-uuid");
    }

    @Test
    void add_ThreeCardsAtOnce_HasThreeCards() {
        inventory.add(sampleCard, 3);
        assertEquals(3, inventory.getCardAmount(sampleCard));
    }

    @Test
    void add_FiveCardsStaggered_HasFiveCards() {
        inventory.add(sampleCard, 2);
        inventory.add(sampleCard, 3);
        assertEquals(5, inventory.getCardAmount(sampleCard));
    }

    @Test
    void AddAndRemoveCards_AddThenRemoveCards_HasThreeCards() {
        inventory.add(sampleCard, 5);
        inventory.remove(sampleCard, 2);
        assertEquals(3, inventory.getCardAmount(sampleCard));
    }

    @Test
    void AddAndRemoveCards_AddThenRemoveAllCards_HasNoCards() {
        inventory.add(sampleCard, 4);
        inventory.remove(sampleCard, 4);
        assertEquals(0, inventory.getCardAmount(sampleCard));
    }

    @Test
    void remove_RemoveMoreCardsThanAvailable_ThrowsRemoveException() {
        inventory.add(sampleCard, 2);
        assertThrows(RemoveException.class, () -> inventory.remove(sampleCard, 3));
    }

    @Test
    void remove_RemoveNonexistentCard_ThrowsRemoveException() {
        assertThrows(RemoveException.class, () -> inventory.remove(sampleCard, 1));
    }
}