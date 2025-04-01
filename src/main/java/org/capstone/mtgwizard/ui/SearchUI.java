package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.boldMediumFont;
import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class SearchUI extends JPanel {

    private JTextField searchField;
    private JButton searchButton;
    private Box resultBox;
    private JScrollPane resultPane;
    private ArrayList<Card> cardsFound;

    public SearchUI() {

        // Intializing layout of tab
        setLayout(new BorderLayout());

        // Initializing search tab with search prompt
        searchField = new JTextField("Search for a card",20);
        searchField.setFont(mediumFont);

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
        searchButton.setFont(mediumFont);

        // Adding action listener for when search button is clicked
        searchButton.addActionListener(new ActionListener() {
            // Performing search
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        // Initializing top search panel that holds search bar and search button
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        resultBox = Box.createVerticalBox();
        resultPane = new JScrollPane(resultBox);

        add(searchPanel, BorderLayout.NORTH);
        add(resultPane, BorderLayout.CENTER);

        setVisible(true);

        cardsFound = new ArrayList<>();

        cardsFound.add(new Card("Epic Card", 5.99f, "5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery"));
    }

    public void performSearch() {
        searchField.setText("Search for a card");

        //resultBox.removeAll();

        for (Card card : cardsFound) {

            SearchResultPanel resultPanel = new SearchResultPanel(card);
            resultBox.add(resultPanel);

        }

        resultPane.updateUI();

    }

}
