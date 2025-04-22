package org.capstone.mtgwizard.domain.service;

import org.capstone.mtgwizard.domain.model.Inventory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class InventoryService {

    AllPrintingsDatabaseHandler allPrintingsDatabaseHandler;

    // Holds currently selected inventory
    public int inventorySelectedIndex = 0;

    // Holds inventories
    ArrayList<Inventory> inventories;
    {
        inventories = new ArrayList<>();
        inventories.add(new Inventory());
        inventories.add(new Inventory());
        inventories.add(new Inventory());
        inventories.add(new Inventory());
        inventories.add(new Inventory());
    }

    public Inventory selectedInventory = inventories.get(inventorySelectedIndex);

    // Holds inventoryFiles
    File[] inventoryFiles = new File[5];

    public InventoryService(AllPrintingsDatabaseHandler allPrintingsDatabaseHandler) {

        this.allPrintingsDatabaseHandler = allPrintingsDatabaseHandler;

        // Creating directory in user files to store inventories
        String homeDir = System.getProperty("user.home");
        String appDataDir = homeDir + File.separator + "MTGWizardData";
        File dataDir = new File(appDataDir);

        // Check if the directory exists, create it if it doesn't
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Successfully created directory: " + dataDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create directory: " + dataDir.getAbsolutePath());
                return; // Exit if directory creation fails
            }
        }

        // Creating inventory files
        inventoryFiles[0] = new File(dataDir, "Inventory1.txt");
        inventoryFiles[1] = new File(dataDir, "Inventory2.txt");
        inventoryFiles[2] = new File(dataDir, "Inventory3.txt");
        inventoryFiles[3] = new File(dataDir, "Inventory4.txt");
        inventoryFiles[4] = new File(dataDir, "Inventory5.txt");
    }

    public void setInventory(int inventoryIndex) {
        inventorySelectedIndex = inventoryIndex;
        selectedInventory = inventories.get(inventorySelectedIndex);
    }

    public void loadInventories() {
        for (int n = 0; n < inventories.size(); n++) {
            inventories.get(n).loadInventory(inventoryFiles[n], allPrintingsDatabaseHandler);
        }
    }

    public void saveInventories() {
        for (int n = 0; n < inventories.size(); n++) {
            inventories.get(n).saveInventory(inventoryFiles[n]);
        }
    }

}
