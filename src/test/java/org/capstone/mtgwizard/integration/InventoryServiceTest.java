package org.capstone.mtgwizard.integration;

import org.capstone.mtgwizard.domain.model.Card;
import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;
import org.capstone.mtgwizard.domain.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private InventoryService inventoryService;
    private AllPrintingsDatabaseHandler databaseHandler;

    @BeforeEach
    void setUp() {
        // All printings handler from local MySQL database for faster testing
        databaseHandler = new AllPrintingsDatabaseHandler("jdbc:mysql://localhost:3306/mtg", "mtguser", "password");
        // Test inventory service
        inventoryService = new InventoryService(databaseHandler);
    }

    @Test
    void editByFile_AddTwoUniqueCards_CorrectCardsAdded(@TempDir Path tempDir) throws IOException {

        // Create a temporary inventory file
        File inventoryFile = tempDir.resolve("inventory.txt").toFile();
        // Adding lines to temp file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFile))) {
            writer.write("Aragorn 2");
            writer.newLine();
            writer.write("Gandalf 3");
            writer.newLine();
            writer.write("Aragorn, King of Gondor");
        }

        // Simulate user uploading test file after clicking add button
        inventoryService.selectedInventory.editByFile(inventoryFile, databaseHandler, "add");

        // Verify that the cards have been added
        Set<Card> cards = inventoryService.selectedInventory.getCards();
        // Assert that 2 Unique cards were added (Aragorn queries return the same cards)
        assertEquals(2, cards.size());

        // Getting cards from generic query
        Card aragornCard = databaseHandler.queryDatabase("Aragorn").get(0);
        Card gandalfCard = databaseHandler.queryDatabase("Gandalf").get(0);

        // Verify that correct card amounts were added
        assertEquals(3, inventoryService.selectedInventory.getCardAmount(aragornCard));
        assertEquals(3, inventoryService.selectedInventory.getCardAmount(gandalfCard));
    }

    @Test
    void editByFile_AddThenRemoveOneUniqueCard_OneCardRemaining(@TempDir Path tempDir) throws IOException {

        // Addings cards to inventory first
        File addFile = tempDir.resolve("add_inventory.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(addFile))) {
            writer.write("Aragorn 2");
        }
        inventoryService.selectedInventory.editByFile(addFile, databaseHandler, "add");

        // Removing one card
        File removeFile = tempDir.resolve("remove_inventory.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(removeFile))) {
            writer.write("Aragorn 1");
        }
        inventoryService.selectedInventory.editByFile(removeFile, databaseHandler, "remove");



        // Verify that the cards have been added
        Set<Card> cards = inventoryService.selectedInventory.getCards();
        // Assert that 1 Unique card exists
        assertEquals(1, cards.size());

        // Getting card from generic query
        Card aragornCard = databaseHandler.queryDatabase("Aragorn").get(0);

        // Verify that 1 aragorn card is left
        assertEquals(1, inventoryService.selectedInventory.getCardAmount(aragornCard));
    }


    @Test
    void editByFile_RemoveTooManyCards_ReturnsError(@TempDir Path tempDir) throws IOException {

        // Add 1 aragorn card
        File addFile = tempDir.resolve("add_inventory.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(addFile))) {
            writer.write("Aragorn 1");
        }
        inventoryService.selectedInventory.editByFile(addFile, databaseHandler, "add");

        // Try to remove 2 aragorn cards
        File removeFile = tempDir.resolve("remove_inventory.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(removeFile))) {
            writer.write("Aragorn 2");
        }
        String errorMessage = inventoryService.selectedInventory.editByFile(removeFile, databaseHandler, "remove");

        // Verify that correct error message is shown
        assertNotNull(errorMessage);
        assertTrue(errorMessage.contains("ERROR: Removed too many"));
        assertTrue(errorMessage.contains("Aragorn"));

        // Make sure aragorn was still removed
        Card aragornCard = databaseHandler.queryDatabase("Aragorn").get(0);
        assertEquals(0, inventoryService.selectedInventory.getCardAmount(aragornCard));
    }


    @Test
    void editByFile_AddNonexistentCard_ReturnsError(@TempDir Path tempDir) throws IOException {
        // Add nonexistent card to inventory
        File addFile = tempDir.resolve("add_nonexistent.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(addFile))) {
            writer.write("NonexistentCard 1");
        }
        String errorString = inventoryService.selectedInventory.editByFile(addFile, databaseHandler, "add");

        // Verify that the error message contains the expected text
        assertTrue(errorString.contains("ERROR: Card <font color='red'>NonexistentCard</font> not found"));
    }
}