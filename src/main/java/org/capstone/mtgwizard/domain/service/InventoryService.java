package org.capstone.mtgwizard.domain.service;

import org.capstone.mtgwizard.domain.model.Inventory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    AllPrintingsDatabaseHandler allPrintingsDatabaseHandler;

    // Holds inventories and their corresponding files
    private List<Inventory> inventories = new ArrayList<>();
    private List<File> inventoryFiles = new ArrayList<>();

    // Holds currently selected inventory index
    public int inventorySelectedIndex = 0;
    public Inventory selectedInventory;

    public InventoryService(AllPrintingsDatabaseHandler allPrintingsDatabaseHandler) {
        this.allPrintingsDatabaseHandler = allPrintingsDatabaseHandler;

        // Creating directory in user files to store inventories
        String homeDir = System.getProperty("user.home");
        String appDataDir = homeDir + File.separator + "MTGWizardData";
        File dataDir = new File(appDataDir);

        // Check if the directory exists, create if it doesn't
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Successfully created directory: " + dataDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create directory: " + dataDir.getAbsolutePath());
                return;
            }
        }

        // Load all existing inventory files
        loadAllInventories(dataDir);

        // If no inventories exist, create initial ones
        if (inventories.size() < 5) {
            createInitialInventories(dataDir);
        }

        // Set initial selected inventory
        if (!inventories.isEmpty()) {
            selectedInventory = inventories.get(0);
        }
    }

    // Load all .txt files from the data directory as inventories
    private void loadAllInventories(File dataDir) {
        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                Inventory inventory = new Inventory();
                // Set name based on file name (remove .txt extension)
                String name = file.getName().substring(0, file.getName().length() - 4);
                inventory.setName(name);
                inventory.loadInventory(file, allPrintingsDatabaseHandler);
                inventories.add(inventory);
                inventoryFiles.add(file);
            }
        }
    }

    // Create initial 5 inventories if none exist
    private void createInitialInventories(File dataDir) {
        for (int i = 1; i <= 5; i++) {

            String name = "Inventory " + i;
            // Sanitize and construct the expected file
            String sanitized = name.replaceAll("[<>:\"/\\|?*]", "_");
            File expectedFile = new File(dataDir, sanitized + ".txt");

            // If file name already exists, don't create new file
            if (!expectedFile.exists()) {
                Inventory inventory = new Inventory(name);
                inventories.add(inventory);
                inventoryFiles.add(expectedFile);
                inventory.saveInventory(expectedFile);
                System.out.println("Created missing inventory: " + name);
            }
        }
    }

    // Create a new inventory with the given name
    public int createNewInventory(String name, File dataDir) throws IOException {
        // Create a unique file for the inventory
        File file = createUniqueFile(dataDir, name);
        if (file != null) {
            Inventory inventory = new Inventory(name);
            inventories.add(inventory);
            inventoryFiles.add(file);
            // Save empty inventory to file
            inventory.saveInventory(file);
            // Return the index of the new inventory
            return inventories.size() - 1;
        }
        return inventorySelectedIndex;
    }

    // Helper to create a unique file (appends number if name exists)
    private File createUniqueFile(File dataDir, String baseName) throws IOException {
        // Sanitize name: replace invalid file characters
        String sanitized = baseName.replaceAll("[<>:\"/\\|?*]", "_");
        File file = new File(dataDir, sanitized + ".txt");

        // If inventory file already exists, don't create inventory
        if (file.exists()) {
            throw new IOException("Inventory already exists");
        } else if (file.createNewFile()) {
            System.out.println("Created new inventory file: " + file.getAbsolutePath());
            return file;
        }

        return null;
    }

    // Get the data directory
    public File getDataDirectory() {
        String homeDir = System.getProperty("user.home");
        return new File(homeDir + File.separator + "MTGWizardData");
    }

    // Get list of inventory names for UI
    public String[] getInventoryNames() {
        String[] names = new String[inventories.size()];
        for (int i = 0; i < inventories.size(); i++) {
            names[i] = inventories.get(i).getName();
        }
        return names;
    }

    // Setting selected inventory from index
    public void setInventory(int inventoryIndex) {
        if (inventoryIndex >= 0 && inventoryIndex < inventories.size()) {
            inventorySelectedIndex = inventoryIndex;
            selectedInventory = inventories.get(inventoryIndex);
        }
    }

    // Removes all entries from currently selected inventory
    public void clearCurrentInventory() {
        if (selectedInventory != null) {
            selectedInventory.removeAll();
        }
    }

    // Saving all inventories to their files
    public void saveInventories() {
        for (int i = 0; i < inventories.size(); i++) {
            inventories.get(i).saveInventory(inventoryFiles.get(i));
        }
    }

    // Function that runs when user clicks add by file inventory button
    public String addByFile() {

        String errorString = "";

        // Getting user supplied file
        File userFile = selectTxtFile();
        if (userFile.isFile()) {
            errorString = selectedInventory.editByFile(userFile, allPrintingsDatabaseHandler, "add");
        }


        return errorString;
    }

    // Function that runs when user clicks remove by file inventory button
    public String removeByFile() {

        String errorString = "";
        // Getting user supplied file
        File userFile = selectTxtFile();
        if (userFile.isFile()) {
            errorString = selectedInventory.editByFile(userFile, allPrintingsDatabaseHandler, "remove");
        }

        return errorString;
    }

    // Custom exception for when user selects incorrect file type
    public class FileTypeException extends RuntimeException {
        public FileTypeException(String message) {
            super(message);
        }
    }

    // Prompts user to select file and returns selected file if it is a .txt file
    private File selectTxtFile() throws FileTypeException {
        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();

        // Set the title of the dialog
        fileChooser.setDialogTitle("Select inventory .txt file");

        // Create a file filter for .txt files
        FileNameExtensionFilter textFilter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(textFilter);

        // Show the file chooser dialog
        int result = fileChooser.showOpenDialog(null); // null for parent component

        File selectedFile;

        // Check if the user selected a file
        if (result == JFileChooser.APPROVE_OPTION) {
            // Getting selected file
            selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            // Verify that it is a .txt file.
            if (isTextFile(selectedFile)) {
                System.out.println("The file is a .txt file.");
            } else {
                System.out.println("The file is not a .txt file.");
                throw new FileTypeException("File is not a .txt file");
            }

        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("User cancelled the operation.");
            return null;
        } else {
            System.out.println("Error occurred while choosing the file.");
            return null;
        }
        return selectedFile;
    }


    // Helper method to check if a file is a .txt file
    private boolean isTextFile(File file) {
        if (file == null) {
            return false;
        }
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            String extension = name.substring(dotIndex + 1).toLowerCase();
            return extension.equals("txt");
        }
        return false;
    }

}
