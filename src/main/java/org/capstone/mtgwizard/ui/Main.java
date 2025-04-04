package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;
import org.capstone.mtgwizard.ui.ProgramFonts;

import javax.swing.*;
import java.awt.*;


public class Main {

    static Card testCard;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {

        // Intializing window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setting default size
        window.setSize(1024, 768);
        window.setTitle("MTG Wizard");

        window.getContentPane().setBackground(Color.BLACK);


        // Initializing tabbed pane to hold tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        window.add(tabbedPane);

        // Initializing search tab
        SearchUI searchUI = new SearchUI();

        // Initializing inventory tab, need to pass tabbed pane so tabs can be switched within it
        InventoryUI inventoryUI = new InventoryUI(tabbedPane, searchUI);

        // Adding search tab to tabbed pane
        tabbedPane.add("Search", searchUI);

        // Adding inventory tab to tabbed pane
        tabbedPane.add("Inventory", inventoryUI);

        // Making it so user can resize window
        window.setResizable(true);

        // Setting window as visible
        window.setVisible(true);

        testCard = new Card("Epic Card", 5.99f, 6.99f, "5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery");

    }
}