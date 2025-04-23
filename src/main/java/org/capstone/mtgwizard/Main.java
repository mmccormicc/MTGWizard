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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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

        // Inventory service
        InventoryService inventoryService = new InventoryService(allPrintingsDatabaseHandler);

        // Loading inventories from files
        inventoryService.loadInventories();

        // Initializing search tab with empty arguments
        SearchTab searchTab = new SearchTab(allPrintingsDatabaseHandler, allPricesDatabaseHandler, inventoryService);

        // Initializing inventory tab, need to pass tabbed pane so tabs can be switched within it
        InventoryTab inventoryTab = new InventoryTab(tabbedPane, searchTab, inventoryService);


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
                }
                // You can add more else-if blocks for additional tabs
            }
        });

        // Setting tab icons
        ImageIcon searchIcon = new ImageIcon("src/main/resources/images/SearchIcon.png");
        tabbedPane.setIconAt(0, searchIcon);
        ImageIcon inventoryIcon = new ImageIcon("src/main/resources/images/InventoryIcon.png");
        tabbedPane.setIconAt(1, inventoryIcon);

        // Listener to handle when window closes
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // Saving inventories on software close
                inventoryService.saveInventories();
            }
        });

        // Setting window as visible
        this.setVisible(true);

    }
}
