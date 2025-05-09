package org.capstone.mtgwizard;

import com.mysql.cj.jdbc.exceptions.SQLError;
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
import java.sql.SQLException;


public class Main extends JFrame {

    public static void main(String[] args) throws SQLException {
        new Main();
    }

    public Main() throws SQLException {

        // Exits program when X clicked
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

        // Setting frame background color
        this.getContentPane().setBackground(Color.BLACK);


        // Initializing tabbed pane to hold tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        // Adding to frame
        this.add(tabbedPane);

        // Creating handler that gets prices using a card's UUID
        AllPricesDatabaseHandler allPricesDatabaseHandler = new AllPricesDatabaseHandler("src/main/resources/prices/AllPricesToday.json");

        // Old handler hosted locally
        //AllPrintingsDatabaseHandler allPrintingsDatabaseHandler = new AllPrintingsDatabaseHandler("jdbc:mysql://localhost:3306/mtg", "mtguser", "password");


        AllPrintingsDatabaseHandler allPrintingsDatabaseHandler = null;
        // Running constructor which connects to MySQL server
        try {
            // Attempt connection to first server
            allPrintingsDatabaseHandler = new AllPrintingsDatabaseHandler(
                    "jdbc:mysql://gondola.proxy.rlwy.net:39906/mtg",
                    "readonly",
                    "readonly_password"
            );
        } catch (RuntimeException e) {
            System.err.println("Error: Couldn't connect to server 1");
            try {
                // Attempt connection to second back up server
                allPrintingsDatabaseHandler = new AllPrintingsDatabaseHandler(
                        "jdbc:mysql://centerbeam.proxy.rlwy.net:37635/mtg",
                        "readonly_user",
                        "secure_password"
                );
            } catch (RuntimeException e2) {
                System.err.println("Error: Couldn't connect to server 2");
            }
        }

        // Inventory service
        InventoryService inventoryService = new InventoryService(allPrintingsDatabaseHandler);
        // Loading inventories from files
        inventoryService.loadInventories();

        // Initializing search tab
        SearchTab searchTab = new SearchTab(allPrintingsDatabaseHandler, allPricesDatabaseHandler, inventoryService);

        // Initializing inventory tab, need to pass tabbed pane so tabs can be switched within it
        InventoryTab inventoryTab = new InventoryTab(tabbedPane, searchTab, inventoryService);

        // Adding search tab to tabbed pane
        tabbedPane.add("Search", searchTab);
        // Adding inventory tab to tabbed pane
        tabbedPane.add("Inventory", inventoryTab);

        // This runs when a tab is clicked on to change tabs
        tabbedPane.addChangeListener(e -> {
            // Getting index of clicked tab
            int selectedIndex = tabbedPane.getSelectedIndex();

            // Updating inventory with new cards that were added when inventory tab is clicked
            if (selectedIndex == 1) {
                inventoryTab.updateInventory();
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
