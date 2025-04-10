package org.capstone.mtgwizard;

import org.capstone.mtgwizard.database.AllPricesDatabaseHandler;
import org.capstone.mtgwizard.database.AllPrintingsDatabaseHandler;
import org.capstone.mtgwizard.ui.InventoryUI;
import org.capstone.mtgwizard.ui.SearchUI;

import javax.swing.*;
import java.awt.*;


public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {

        // Intializing window
        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setting default size
        window.setSize(1024, 768);
        // Setting window title
        window.setTitle("MTG Wizard");
        // Window appears in middle of screen
        window.setLocationRelativeTo(null);
        // Making it so user can resize window
        window.setResizable(true);

        // Getting application icon
        ImageIcon icon = new ImageIcon("src/main/resources/images/WizardIcon.png");
        // Setting application icons
        window.setIconImage(icon.getImage());

        // Setting background color
        window.getContentPane().setBackground(Color.BLACK);


        // Initializing tabbed pane to hold tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        window.add(tabbedPane);

        // Creating handler that gets prices using a card's UUID
        AllPricesDatabaseHandler allPricesDatabaseHandler = new AllPricesDatabaseHandler("src/main/resources/prices/AllPricesToday.json");

        // Creating handler that queries mtg database
        AllPrintingsDatabaseHandler allPrintingsDatabaseHandler = new AllPrintingsDatabaseHandler("jdbc:mysql://localhost:3306/mtg", "mtguser", "password");

        // Initializing search tab
        SearchUI searchUI = new SearchUI(allPrintingsDatabaseHandler, allPricesDatabaseHandler);

        // Initializing inventory tab, need to pass tabbed pane so tabs can be switched within it
        InventoryUI inventoryUI = new InventoryUI(tabbedPane, searchUI);

        // Adding search tab to tabbed pane
        tabbedPane.add("Search", searchUI);

        // Adding inventory tab to tabbed pane
        tabbedPane.add("Inventory", inventoryUI);

        // Setting window as visible
        window.setVisible(true);

    }
}
