package org.capstone.mtgwizard.domain.service;

import org.capstone.mtgwizard.domain.model.Inventory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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

    public String addByFile() {

        String errorString = "";

        // Getting user supplied file
        File userFile = selectTxtFile();
        if (userFile.isFile()) {
            errorString = selectedInventory.editByFile(userFile, allPrintingsDatabaseHandler, "add");
        }


        return errorString;
    }

    public String removeByFile() {

        String errorString = "";
        // Getting user supplied file
        File userFile = selectTxtFile();
        if (userFile.isFile()) {
            errorString = selectedInventory.editByFile(userFile, allPrintingsDatabaseHandler, "remove");
        }

        return errorString;
    }

    public class FileTypeException extends RuntimeException {
        public FileTypeException(String message) {
            super(message);
        }
    }

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

    public void clearCurrentInventory() {
        selectedInventory.removeAll();
    }

}
