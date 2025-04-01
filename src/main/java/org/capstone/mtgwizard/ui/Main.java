package org.capstone.mtgwizard.ui;

import javax.swing.*;

public class Main {
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

    }
}