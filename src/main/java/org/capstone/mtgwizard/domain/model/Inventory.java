package org.capstone.mtgwizard.domain.model;

import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class Inventory {

    private String name;

    // This holds a map of cards to their quantity in inventory
    // It is a tree map so that cards are sorted alphabetically on put method
    private TreeMap<Card, Integer> inventoryEntries = new TreeMap<>();

    // Updated constructor to accept name
    public Inventory(String name) {
        this.name = name;
    }

    // Default constructor for backward compatibility
    public Inventory() {
        this.name = "Unnamed Inventory";
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Function to edit inventory based on an inventory file and "add" or "remove" editOption
    public String editByFile(File inventoryFile, AllPrintingsDatabaseHandler allPrintingsDatabaseHandler, String editOption) {

        // This holds the string that is displayed to the user when errors are encountered during the edit by file process
        String errorString = "";

        // Catches file not found exceptions
        try {
            // Creating new file reader to read file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inventoryFile));

            // Holds line in file
            String line;
            // Read the file line by line
            while ((line = bufferedReader.readLine()) != null) {

                // Splitting into search terms and quantity
                // Line looks like 'Aragorn 1'
                String[] entryWords = line.split(" ");

                // If inventory line is not blank
                if (entryWords.length > 0) {

                    // Gets last word in line (Usually card quantity unless not specified)
                    String lastWord = entryWords[entryWords.length - 1];

                    // Initializing query as whole line
                    String query = line;

                    // Holds if line has quantity at end or not
                    boolean hasQuantity = false;

                    // Holds cards found by query
                    ArrayList<Card> foundCards;

                    // If inventory line has quantity of cards at the end
                    if (isInteger(lastWord)) {
                        // Removing quantity from query
                        query = line.substring(0, line.length() - lastWord.length() - 1);
                        // Line has quantity
                        hasQuantity = true;
                    }

                    // If query is a Uuid (No card name is this length)
                    if (entryWords[0].length() == 36) {
                        foundCards = allPrintingsDatabaseHandler.queryByUuid(query);
                        // Query is not a uuid
                    } else {
                        //  Querying database
                        foundCards = allPrintingsDatabaseHandler.queryDatabase(query);
                    }

                    // If cards were found
                    if(!foundCards.isEmpty()) {
                        // If quantity was specified
                        if (hasQuantity) {
                            if (editOption.equals("add")) {
                                // Adding card to inventory with quantity specified in file. Only adding first card found.
                                add(foundCards.get(0), Integer.valueOf(lastWord));
                            } else if (editOption.equals("remove")) {
                                try {
                                    // Trying to remove found cards from inventory
                                    remove(foundCards.get(0), Integer.valueOf(lastWord));
                                } catch (RemoveException e) {
                                    // If too many cards removed display error message
                                    errorString += "ERROR: Removed too many <font color='red'>" + query + "</font> from inventory<br>";
                                }
                            }
                        } else {
                            if (editOption.equals("add")) {
                                // Adding 1 card if quantity wasn't specified
                                add(foundCards.get(0), 1);
                            } else if (editOption.equals("remove")) {
                                // Removing 1 card if quantity wasn't specified
                                try {
                                    // Trying to remove found card from inventory
                                    remove(foundCards.get(0), 1);
                                } catch (RemoveException e) {
                                    // If card not in inventory display error message
                                    errorString += "ERROR: Card <font color='red'>" + query + "</font> not in inventory<br>";
                                }
                            }
                        }
                        // Card was not found by query
                    } else {
                        // If line isn't blank
                        if (!query.equals("")) {
                            // Adding error message of missing card
                            errorString += "ERROR: Card <font color='red'>" + query + "</font> not found<br>";
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

        return errorString;
    }

    // Tests if string is an integer
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

                // Splitting into uuid and quantity
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

    // Saving inventory to file
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
        // Card initially not found
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

    // Custom exception for when cards are removed beyond what is in inventory
    public static class RemoveException extends RuntimeException {
        public RemoveException(String message) {
            super(message);
        }
    }

    // Remove cards from inventory with quantity specified
    public void remove(Card card, int n) {

        // Looking for existing card in inventory by UUID
        // Card initially not found
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
            // If request removes more than number of cards in inventory
            if(inventoryEntries.get(foundCard) < n) {
                // Remove card and quantity from inventory
                inventoryEntries.remove(foundCard);
                // Throw overdraw error
                throw new RemoveException("Removed more cards than are in inventory.");
                // Removing all cards from inventory exactly
            } else if (inventoryEntries.get(foundCard) == n) {
                // Remove card and quantity from inventor
                inventoryEntries.remove(foundCard);
            } else {
                // Reduce card by specified quantity
                inventoryEntries.put(foundCard, inventoryEntries.get(foundCard) - n);
            }
            // Card not in inventory
        } else {
            throw new RemoveException("No cards left in inventory.");
        }
    }

    public void removeAll() {
        inventoryEntries.clear();
    }

    // Returns unique cards in inventory (Not their quantity)
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
