package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;
import org.capstone.mtgwizard.ui.ProgramFonts;

import javax.swing.*;



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
        window.setSize(800, 600);
        window.setTitle("MTG Wizard");

        // Initializing search tab
        SearchUI searchUI = new SearchUI();

        // Initializing tabbed pane to hold tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        window.add(tabbedPane);

        // Adding search tab to tabbed pane
        tabbedPane.add("Search", searchUI);

        // Making it so user can resize window
        window.setResizable(true);

        // Setting window as visible
        window.setVisible(true);

        testCard = new Card("Epic Card", 5.99f, "5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery");

    }
}