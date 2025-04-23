package org.capstone.mtgwizard.domain.model;

import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Inventory {
    private HashMap<Card, Integer> inventoryEntries = new HashMap<>();

    public void addByFile(File inventoryFile, AllPrintingsDatabaseHandler allPrintingsDatabaseHandler) {
        // Catches file not found exceptions
        try {
            // Creating new file reader to read file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inventoryFile));

            // Holds line in file
            String line;
            // Read the file line by line
            while ((line = bufferedReader.readLine()) != null) {

                // Splitting into search terms and quantity
                String[] entryWords = line.split(" ");

                // If inventory line is not blank
                if (entryWords.length > 0) {

                    String lastWord = entryWords[entryWords.length - 1];

                    ArrayList<Card> foundCards;

                    // If inventory line has quantity of cards at the end
                    if (isInteger(lastWord)) {
                        // Removing quantity from query
                        String query = line.substring(0, line.length() - lastWord.length() - 1);
                        System.out.println(query);

                        //  Querying database
                        foundCards = allPrintingsDatabaseHandler.queryDatabase(query);

                        // If cards were found
                        if(foundCards.size() >= 1) {
                            // Adding card to inventory with quantity specified in file. Only adding first card found.
                            add(foundCards.get(0), Integer.valueOf(lastWord));
                        }
                    // No quantity supplied
                    } else {

                        // Querying database with whole line
                        foundCards = allPrintingsDatabaseHandler.queryDatabase(line.trim());

                        // If cards were found
                        if(foundCards.size() >= 1) {
                            // Adding card to inventory with quantity of 1. Only adding first card found.
                            add(foundCards.get(0), 1);
                        }
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

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
                        add(foundCards.get(0), Integer.valueOf(entryWords[1]));
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

        // Looking for existing card in inventory by UUID

        Card foundCard = null;
        // If there are cards in inventory
        if (inventoryEntries != null) {
            // For each card in inventory
            for (Card c : inventoryEntries.keySet()) {
                // Testing if UUid matches supplied card
                if (c.getUuid().equals(card.getUuid())) {
                    // Card found
                    foundCard = c;
                }
            }
        }

        // If inventory contains card to be added
        if(foundCard != null) {
            // Updating quantity using existing card key
            inventoryEntries.put(foundCard, inventoryEntries.get(foundCard) + n);
        } else {
            // Adding new card with quantity specified
            inventoryEntries.put(card, n);
        }

    }

    // Remove cards from inventory with quantity specified
    public void remove(Card card, int n) {

        // Looking for existing card in inventory by UUID

        Card foundCard = null;
        // If there are cards in inventory
        if (inventoryEntries != null) {
            // For each card in inventory
            for (Card c : inventoryEntries.keySet()) {
                // Testing if UUid matches supplied card
                if (c.getUuid().equals(card.getUuid())) {
                    // Card found
                    foundCard = c;
                }
            }
        }

        // If inventory contains card to be removed
        if(foundCard != null) {
            // If request removes more or equal number of cards in inventory
            if(inventoryEntries.get(foundCard) <= n) {
                // Remove card and quantity from inventory
                inventoryEntries.remove(foundCard);
            } else {
                // Reduce card by specified quantity
                inventoryEntries.put(foundCard, inventoryEntries.get(foundCard) - n);
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
