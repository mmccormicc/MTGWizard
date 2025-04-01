package org.capstone.mtgwizard.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

public class SearchUI extends JPanel {

    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultArea;
    private List<String> data;

    public SearchUI() {

        // Intializing layout of tab
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Initializing search tab with search prompt
        searchField = new JTextField("Search for a card",20);

        // Adding focus listener to remove search prompt when clicked
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        // Initializing search button
        searchButton = new JButton("Search");

        // Adding action listener for when search button is clicked
        searchButton.addActionListener(new ActionListener() {
            // Resetting search prompt
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText("Search for a card");
            }
        });

        // Initializing top search panel that holds search bar and search button
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        resultArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(searchPanel);
        add(resultArea);

        setVisible(true);
    }

}
