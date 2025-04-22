package org.capstone.mtgwizard;

import org.capstone.mtgwizard.domain.service.AllPricesDatabaseHandler;
import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;
import org.capstone.mtgwizard.domain.service.InventoryService;
import org.capstone.mtgwizard.ui.InventoryTab;
import org.capstone.mtgwizard.ui.SearchTab;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class Main extends JFrame {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {

        // Intializing window
        //Frame this = new JFrame();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setting default size
        this.setSize(1024, 768);
        // Setting window title
        this.setTitle("MTG Wizard");
        // Window appears in middle of screen
        this.setLocationRelativeTo(null);
        // Making it so user can resize window
        this.setResizable(true);

        // Getting application icon
        ImageIcon icon = new ImageIcon("src/main/resources/images/WizardIcon.png");
        // Setting application icons
        this.setIconImage(icon.getImage());

        // Setting background color
        this.getContentPane().setBackground(Color.BLACK);


        // Initializing tabbed pane to hold tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        this.add(tabbedPane);

        // Creating handler that gets prices using a card's UUID
        AllPricesDatabaseHandler allPricesDatabaseHandler = new AllPricesDatabaseHandler("src/main/resources/prices/AllPricesToday.json");

        // Creating handler that queries mtg database
        AllPrintingsDatabaseHandler allPrintingsDatabaseHandler = new AllPrintingsDatabaseHandler("jdbc:mysql://localhost:3306/mtg", "mtguser", "password");

        // Initializing search tab with empty arguments
        SearchTab searchTab = new SearchTab(null, null, null);

        // Inventory service
        InventoryService inventoryService = new InventoryService(allPrintingsDatabaseHandler);

        // Loading inventories from files
        inventoryService.loadInventories();

        // Initializing inventory tab, need to pass tabbed pane so tabs can be switched within it
        InventoryTab inventoryTab = new InventoryTab(tabbedPane, searchTab, inventoryService);

        // Intializing search tab with arguments now including inventoryTab
        searchTab = new SearchTab(allPrintingsDatabaseHandler, allPricesDatabaseHandler, inventoryService);

        // Adding search tab to tabbed pane
        tabbedPane.add("Search", searchTab);
        // Adding inventory tab to tabbed pane
        tabbedPane.add("Inventory", inventoryTab);

        // Add the ChangeListener to the JTabbedPane
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();

                if (selectedIndex == 1) {
                    inventoryTab.updateInventory();
                    inventoryService.saveInventories();
                }
                // You can add more else-if blocks for additional tabs
            }
        });

        // Setting tab icons
        ImageIcon searchIcon = new ImageIcon("src/main/resources/images/SearchIcon.png");
        tabbedPane.setIconAt(0, searchIcon);
        ImageIcon inventoryIcon = new ImageIcon("src/main/resources/images/InventoryIcon.png");
        tabbedPane.setIconAt(1, inventoryIcon);

        // Setting window as visible
        this.setVisible(true);

    }
}
