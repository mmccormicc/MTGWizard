package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.database.AllPricesDatabaseHandler;
import org.capstone.mtgwizard.database.AllPrintingsDatabaseHandler;
import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.largeFont;
import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class SearchUI extends JPanel {

    private AllPrintingsDatabaseHandler allPrintingsDatabaseHandler;
    private AllPricesDatabaseHandler pricesHandler;

    private JTextField searchField;
    private JButton searchButton;
    private JButton helpButton;

    private JPanel resultPanel;
    private JLabel resultLabel;

    private Box resultBox;
    private JScrollPane resultScrollPanel;

    private CardPanel cardPanel;
    private ArrayList<Card> cardsFound;

    private JPanel backPanel;
    private JButton backButton;

    private Boolean showingCard = false;

    public SearchUI(AllPrintingsDatabaseHandler printingsHandler, AllPricesDatabaseHandler pricesHandler) {

        this.allPrintingsDatabaseHandler = printingsHandler;
        this.pricesHandler = pricesHandler;

        // Intializing layout of tab
        setLayout(new BorderLayout());

        //////////////////////
        // SEARCH PANEL
        ////////////////////

        // Initializing search tab with search prompt
        searchField = new JTextField("Search for a card",20);
        searchField.setFont(mediumFont);

        // Adding focus listener to remove search prompt when clicked
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(searchField.getText().equals("Search for a card")) {
                    searchField.setText("");
                }
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

        // Initializing help button
        helpButton = new JButton("Help");
        helpButton.setFont(mediumFont);

        // Adding action listener for when search button is clicked
        helpButton.addActionListener(new ActionListener() {
            // Performing search
            @Override
            public void actionPerformed(ActionEvent e) {

                //"<html><font face='Arial' size='5' color='blue'><b>This is a message with a custom font.</b></font></html>";

                JOptionPane.showMessageDialog(null,
                        "<html><font face='Arial' size='4' color='black'>" +
                                "Search Option 1: Enter a card name or part of a card name in search box.<br>" +
                                "Example - <font color='blue'>black lotus</font><br><br>" +
                                "Search Option 2: Format search with tags.<br>" +
                                " Put a space between tags, and use set code for set. <br>" +
                                "Example - <font color='blue'>name:black lotus</font> <font color='green'>set:LEA</font><br>" +
                                "Example - <font color='green'>set:10E</font><br>" +
                                "</font></html>",
                        "Search Help",
                        JOptionPane.QUESTION_MESSAGE);
            }
        });

        // Initializing top search panel that holds search bar and search button
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.BLACK);

        // Initializing left panel that goes on left side of search panel and holds help button
        JPanel leftPanel = new JPanel(new FlowLayout());
        leftPanel.setBackground(Color.BLACK);
        leftPanel.add(helpButton);
        leftPanel.add(Box.createRigidArea(new Dimension(200, 1)));

        // Adding components to search panel
        searchPanel.add(leftPanel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);




        //////////////////////
        // RESULT PANEL
        ////////////////////

        // Initializing box that holds search results
        resultBox = Box.createVerticalBox();
        // Initializing scrollable pane of search results
        resultScrollPanel = new JScrollPane(resultBox);

        // Creating result panel
        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        // Label that shows number of results
        resultLabel = new JLabel();
        resultLabel.setFont(mediumFont);

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(resultScrollPanel);


        // Adding search and result panels with border layout constraints
        add(searchPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);



        //////////////////////
        // CARD PANEL
        ////////////////////

        // Creating card panel that will hold card info, initially not shown
        cardPanel = new CardPanel();

        // Initializing arraylist that holds cards found by search
        cardsFound = new ArrayList<>();

        // Intializing back panel
        backPanel = new JPanel();
        backPanel.setLayout(new FlowLayout());

        // Creating back button
        backButton = new JButton("Back");
        backButton.setFont(largeFont);

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
        cardsFound = allPrintingsDatabaseHandler.queryDatabase(searchField.getText());

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

        card.setCardKingdomPrice(pricesHandler.getPrice("cardkingdom", card.getUuid()));
        card.setTCGPlayerPrice(pricesHandler.getPrice("tcgplayer", card.getUuid()));
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
