package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.boldMediumFont;
import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class SearchUI extends JPanel {

    private JTextField searchField;
    private JButton searchButton;
    private Box resultBox;
    private JScrollPane resultPanel;
    private JPanel cardPanel;
    private ArrayList<Card> cardsFound;

    public Card testCard;

    public SearchUI() {

        testCard = new Card("Epic Card", 5.99f, "5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery");


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
        searchPanel.setBackground(Color.BLACK);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Initializing box that holds search results
        resultBox = Box.createVerticalBox();
        // Initializing scrollable pane of search results
        resultPanel = new JScrollPane(resultBox);
        
        cardPanel = new CardPanel(testCard);

        // Adding search and result panels with border layout constraints
        add(searchPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);

        // Initializing arraylist that holds cards found by search
        cardsFound = new ArrayList<>();

        // Test cards
        cardsFound.add(new Card("Epic Card", 5.99f, "5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery"));

        cardsFound.add(new Card("Lame Card", 5.99f, "5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery"));
    }

    public void performSearch() {
        // Resetting search bar text
        searchField.setText("Search for a card");

        // Adds cards to scrollable pane for each card found
        for (Card card : cardsFound) {

            // Creating new search panel entry
            SearchResultPanel resultPanel = new SearchResultPanel(card, this);
            // Adding entry to search result box
            resultBox.add(resultPanel);

        }

        // Updating scrollable pane
        resultPanel.updateUI();

    }
    
    public void displayCardInfo(Card card) {
        remove(resultPanel);
        add(cardPanel);
        updateUI();
    }

}
