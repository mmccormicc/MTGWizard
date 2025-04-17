package org.capstone.mtgwizard.domain.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public void saveInventory(File inventoryFile) {
        // Write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFile, false))) {
            for (Card card : getCards()) {
                writer.write(card.getUuid() + " " + getCardAmount(card));
                writer.newLine(); // Add a new line
            }
            System.out.println("Successfully wrote to file: " + inventoryFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void add(Card card, int n) {
        if(inventoryEntries != null && inventoryEntries.containsKey(card)) {
            inventoryEntries.put(card, inventoryEntries.get(card) + n);
        } else {
            inventoryEntries.put(card, n);
        }

        for (Card c : getCards()) {
            System.out.println(c.getName() + " " + inventoryEntries.get(c));
        }
        System.out.println();
    }

    public Set<Card> getCards() {
        return inventoryEntries.keySet();
    }

    public int getCardAmount(Card card) {
        if(inventoryEntries.containsKey(card)) {
            return inventoryEntries.get(card);
        }
        return 0;
    }

}
