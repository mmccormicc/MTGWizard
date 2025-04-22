package org.capstone.mtgwizard.domain.model;

import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Inventory {
    private HashMap<Card, Integer> inventoryEntries = new HashMap<>();

    public void addByFile() {

    }

    public void removeByFile() {

    }

    public void sortAlphabetically() {

    }

    // Loads inventory from file associated with inventory
    public void loadInventory(File inventoryFile, AllPrintingsDatabaseHandler allPrintingsDatabaseHandler) {
        // Catches file not found exceptions
        try {
            // Creating new file reader to read file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inventoryFile));

            // Holds line in file
            String line;
            // Read the file line by line
            while ((line = bufferedReader.readLine()) != null) {

                // Spllitting into uuid and quantity
                String[] entryWords = line.split(" ");

                // If uuid and quantity are present in line
                if (entryWords.length >= 2) {
                    // Getting list of found cards from uuid. Should always have size of 0 or 1
                    ArrayList<Card> foundCards = allPrintingsDatabaseHandler.queryByUuid(entryWords[0]);
                    // If a card was found
                    if(foundCards.size() >= 1) {
                        // Adding card to inventory with quantity specified in file
                        inventoryEntries.put(foundCards.get(0), Integer.valueOf(entryWords[1]));
                    }
                }
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            // Handle the case where the file does not exist
            System.err.println("Error: The file '" + inventoryFile.getAbsolutePath() + "' was not found!");
        } catch (IOException e) {
            // Handle other IO exceptions, such as errors during reading
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public void saveInventory(File inventoryFile) {
        // Write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFile, false))) {
            // For each card in inventory
            for (Card card : getCards()) {
                // Writing uuid and quantity to line in file
                writer.write(card.getUuid() + " " + getCardAmount(card));
                // Add new line
                writer.newLine();
            }
            // Confirmation message
            System.out.println("Successfully wrote to file: " + inventoryFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add card to inventory with quantity specified
    public void add(Card card, int n) {
        // If inventory contains entries and contains card to be added
        if(inventoryEntries != null && inventoryEntries.containsKey(card)) {
            // Increasing card quantity
            inventoryEntries.put(card, inventoryEntries.get(card) + n);
        } else {
            // Adding new card with quantity specified
            inventoryEntries.put(card, n);
        }

        // Testing cards got added
        for (Card c : getCards()) {
            System.out.println(c.getName() + " " + inventoryEntries.get(c));
        }
        System.out.println();
    }

    // Remove cards from inventory with quantity specified
    public void remove(Card card, int n) {
        // If inventory contains entries and contains card to be removed
        if(inventoryEntries != null && inventoryEntries.containsKey(card)) {
            // If request removes more or equal number of cards in inventory
            if(inventoryEntries.get(card) <= n) {
                // Remove card and quantity from inventory
                inventoryEntries.remove(card);
            } else {
                // Reduce card by specified quantity
                inventoryEntries.put(card, inventoryEntries.get(card) - n);
            }
        }
    }

    // Returns cards in inventory
    public Set<Card> getCards() {
        return inventoryEntries.keySet();
    }

    // Gets amount of card in inventory
    public int getCardAmount(Card card) {
        if(inventoryEntries.containsKey(card)) {
            return inventoryEntries.get(card);
        }
        return 0;
    }
}
