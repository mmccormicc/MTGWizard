package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setTitle("MTG Wizard");

        SearchUI searchUI = new SearchUI();

        JTabbedPane tabbedPane = new JTabbedPane();
        window.add(tabbedPane);

        tabbedPane.add("Search", searchUI);

        window.setVisible(true);
        window.setResizable(true);

    }
}