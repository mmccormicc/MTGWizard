package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.domain.service.AllPricesDatabaseHandler;
import org.capstone.mtgwizard.domain.service.AllPrintingsDatabaseHandler;
import org.capstone.mtgwizard.domain.model.Card;
import org.capstone.mtgwizard.domain.service.InventoryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.largeFont;
import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class SearchTab extends JPanel {

    private AllPrintingsDatabaseHandler printingsHandler;
    private AllPricesDatabaseHandler pricesHandler;

    private JTextField searchField;

    private JDialog helpDialogue;

    private JPanel resultPanel;
    private JLabel resultLabel;

    private Box resultBox;
    private JScrollPane resultScrollPanel;

    private CardInfoPanel cardInfoPanel;
    private ArrayList<Card> cardsFound;
    private Boolean showingCard = false;

    private JPanel backPanel;


    public SearchTab(AllPrintingsDatabaseHandler printingsHandler, AllPricesDatabaseHandler pricesHandler, InventoryService inventoryService) {

        this.printingsHandler = printingsHandler;
        this.pricesHandler = pricesHandler;

        // Intializing layout of tab
        setLayout(new BorderLayout());

        //////////////////////
        // SEARCH PANEL
        ////////////////////

        // Initializing search tab with search prompt
        searchField = new JTextField("Search for a card",20);
        searchField.setFont(mediumFont);

        // Adding focus listener to remove search prompt text when clicked
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Removing search text
                if(searchField.getText().equals("Search for a card")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        // Key listener for when enter is pressed while searching
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Initializing search when enter pressed
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        performSearch();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        // Initializing search button
        JButton searchButton = new JButton("Search");
        searchButton.setFont(mediumFont);

        // Adding action listener for when search button is clicked
        searchButton.addActionListener(new ActionListener() {
            // Performing search
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        // Creating dialogue but not displaying it yet
        helpDialogue = createHelpDialog();

        // Initializing help button
        JButton helpButton = new JButton("Help");
        helpButton.setFont(mediumFont);

        // Adding action listener for when search button is clicked
        helpButton.addActionListener(new ActionListener() {
            // Performing search
            @Override
            public void actionPerformed(ActionEvent e) {
                // Starting dialogue in separate thread so main window can still be interacted with
                SwingUtilities.invokeLater(() -> {
                    helpDialogue.setVisible(true);
                });
            }
        });

        // Initializing top search panel that holds search bar and search button
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.BLACK);

        // Initializing left panel that goes on left side of search panel and holds help button
        JPanel leftPanel = new JPanel(new FlowLayout());
        leftPanel.setBackground(Color.BLACK);
        leftPanel.add(helpButton);
        leftPanel.add(Box.createRigidArea(new Dimension(100, 1)));

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

        // Getting vertical scroll bar and increasing scroll speed
        JScrollBar verticalScrollBar = resultScrollPanel.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(verticalScrollBar.getUnitIncrement() * 10);

        // Creating results panel
        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        // Label that shows number of results
        resultLabel = new JLabel();
        resultLabel.setFont(mediumFont);
        resultLabel.setForeground(Color.RED);

        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(resultScrollPanel);


        // Adding search and result panels with border layout constraints
        add(searchPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);



        //////////////////////
        // CARD PANEL
        ////////////////////

        // Creating card panel that will hold card info, initially not shown
        cardInfoPanel = new CardInfoPanel(inventoryService);

        // Initializing arraylist that holds cards found by search
        cardsFound = new ArrayList<>();

        // Intializing back panel
        backPanel = new JPanel();
        backPanel.setLayout(new FlowLayout());

        // Creating back button
        JButton backButton = new JButton("Back To Search");
        backButton.setFont(largeFont);

        // Adding action listener for when back button is clicked
        backButton.addActionListener(new ActionListener() {
            // Going back
            @Override
            public void actionPerformed(ActionEvent e) {
                // Removing card and back panels
                remove(cardInfoPanel);
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

    // Search for card that was entered in search box
    public void performSearch() {

        // Querying database from search text
        cardsFound = printingsHandler.queryDatabase(searchField.getText());

        // If card info is being shown
        if (showingCard) {
            // Switching from card panel to results panel
            remove(cardInfoPanel);
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
                // Showing number of results less than 100
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

        // Updating scrollable pane now that all result panels added
        resultScrollPanel.updateUI();

    }

    // Create the JDialog for help window
    private static JDialog createHelpDialog() {
        // Creatine new dialog
        JDialog dialog = new JDialog(null, "Search Help", Dialog.ModalityType.MODELESS);
        // Dialog settings
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(700, 400);
        dialog.setLocationRelativeTo(null);

        // Help message to be displayed
        String message = "<html><font face='Arial' size='4' color='black'>" +
                "Search Option 1: Enter a card name or part of a card name in search box.<br><br>" +
                "Example - <font color='blue'>sol ring</font><br><br>" +
                "Search Option 2: Format search with tags.<br>" +
                "Put a space between tags, and use 3 or 4 letter set code for set. <br>" +
                "Set code can be found in bottom left corner of modern cards. <br><br>" +
                "Example - <font color='blue'>name:sol ring</font> <font color='green'>set:C13</font><br><br>" +
                "You can enter only a set code to see all cards from a set. <br><br>" +
                "Example - <font color='green'>set:10E</font><br><br><br>" +
                "Note: Letter case does not matter for searches." +
                "</font></html>";

        // Creating label with message
        JLabel label = new JLabel(message);
        // Centering label
        label.setHorizontalAlignment(SwingConstants.CENTER);
        // Adding label to dialog
        dialog.add(label);

        return dialog;
    }

    // Switching from result panel to card panel
    public void displayCardInfo(Card card) {
        // Removing results
        remove(resultPanel);

        // Retrieving prices of card to display
        card.setCardKingdomPrice(pricesHandler.getPrice("cardkingdom", card.getUuid()));
        card.setTCGPlayerPrice(pricesHandler.getPrice("tcgplayer", card.getUuid()));

        // Setting card panel to display info of card
        cardInfoPanel.setCard(card);

        // Adding card panel and back button panel to searchTab
        add(cardInfoPanel, BorderLayout.CENTER);
        add(backPanel, BorderLayout.SOUTH);

        // Now showing card
        showingCard = true;

        // Updating search tab
        updateUI();
    }

}
