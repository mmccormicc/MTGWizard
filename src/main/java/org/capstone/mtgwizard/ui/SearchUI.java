package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.database.DatabaseHandler;
import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class SearchUI extends JPanel {

    private DatabaseHandler databaseHandler;

    private JTextField searchField;
    private JButton searchButton;

    private JPanel resultPanel;
    private JLabel resultLabel;

    private Box resultBox;
    private JScrollPane resultScrollPanel;

    private CardPanel cardPanel;
    private ArrayList<Card> cardsFound;

    private JPanel backPanel;
    private JButton backButton;

    public Card testCard;

    private Boolean showingCard = false;

    public SearchUI(DatabaseHandler databaseHandler) {

        this.databaseHandler = databaseHandler;

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
                performSearch(searchField.getText());
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
        resultScrollPanel = new JScrollPane(resultBox);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        resultLabel = new JLabel();
        resultLabel.setFont(mediumFont);

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(resultScrollPanel);

        // Adding search and result panels with border layout constraints
        add(searchPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);

        // Creating card panel that will hold card info, initially not shown
        cardPanel = new CardPanel();

        // Initializing arraylist that holds cards found by search
        cardsFound = new ArrayList<>();

        // Test cards
        cardsFound.add(new Card("Epic Card", 5.99f, 6.99f,"5EE", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery"));

        cardsFound.add(new Card("Questing Beast", 5.99f, 6.99f,"2GG",
                "Vigilance, deathtouch, haste\n" +
                "Questing Beast can't be blocked by creatures\n" +
                "with power 2 or less.\n" +
                "Combat damage that would be dealt by\n" +
                "creatures you control can't be prevented.\n" +
                "Whenever Questing Beast deals combat damage\n" +
                "to an opponent, it deals that much damage to\n" +
                "target planeswalker that player controls.",
                "Eldraine", "Legendary Creature"));

        // Intializing back panel
        backPanel = new JPanel();
        backPanel.setLayout(new FlowLayout());

        // Creating back button
        backButton = new JButton("Go Back");
        backButton.setFont(mediumFont);

        // Adding action listener for when back button is clicked
        backButton.addActionListener(new ActionListener() {
            // Going back
            @Override
            public void actionPerformed(ActionEvent e) {
                // Removing card and back panels
                remove(cardPanel);
                remove(backPanel);
                // Readding result panel
                add(resultPanel);
                // Card no longer being shown
                showingCard = false;
                // Updating UI
                updateUI();
            }
        });
        // Adding button to back panel
        backPanel.add(backButton);
    }

    public void performSearch(String searchText) {

        // Querying database from search text
        cardsFound = databaseHandler.queryDatabase(searchField.getText());

        // Resetting search bar text
        searchField.setText("Search for a card");

        // If card info is being shown
        if (showingCard) {
            // Switching from card panel to results panel
            remove(cardPanel);
            remove(backPanel);
            add(resultPanel);
            showingCard = false;
        }

        // Number of results label
        if (cardsFound.size() > 0) {
            // If max results are being shown
            if (cardsFound.size() == 100) {
                // Max result text
                resultLabel.setText("Showing max of 100 results");
            } else {
                // Showing number of results
                resultLabel.setText("Results: " + cardsFound.size());
            }
        } else {
            // No results found shown
            resultLabel.setText("No Results Found");
        }

        // Removing previous search results
        resultBox.removeAll();

        // Adds cards to scrollable pane for each card found
        for (Card card : cardsFound) {

            // Creating new search panel entry for each card
            SearchResultPanel searchResultPanel = new SearchResultPanel(card, this);
            // Adding entry to search result box
            resultBox.add(searchResultPanel);

        }

        // Updating scrollable pane
        resultScrollPanel.updateUI();

    }

    // Switching from result panel to card panel
    public void displayCardInfo(Card card) {
        // Removing results
        remove(resultPanel);
        // Setting card panel to display info of card
        cardPanel.setCard(card);
        // Adding card panel and back button panel to searchUI
        add(cardPanel, BorderLayout.CENTER);
        add(backPanel, BorderLayout.SOUTH);

        // Now showing card
        showingCard = true;

        updateUI();
    }

}
