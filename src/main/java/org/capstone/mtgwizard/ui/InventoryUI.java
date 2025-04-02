package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class InventoryUI extends JPanel {

    private JPanel leftPanel;
    private JButton tempButton;
    private JButton addByFileButton;
    private JButton removeByFileButton;

    private Box inventoryBox;
    private JScrollPane inventoryPanel;

    private ArrayList<Card> inventoryCards;

    private JTabbedPane tabbedPane;
    private SearchUI searchUI;

    InventoryUI(JTabbedPane tabbedPane, SearchUI searchUI) {
        // Setting tabbed pane so it can be passed
        this.tabbedPane = tabbedPane;
        // Setting search ui
        this.searchUI = searchUI;

        // Setting layout
        setLayout(new BorderLayout());

        // LEFT BUTTONS PANEL

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        tempButton = new JButton("Inventory 1");
        tempButton.setFont(mediumFont);
        tempButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        tempButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        leftPanel.add(tempButton);

        // Making empty space between buttons
        leftPanel.add(Box.createRigidArea(new Dimension(10, 50)));

        addByFileButton = new JButton("Add With File");
        addByFileButton.setFont(mediumFont);
        addByFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addByFileButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        addByFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInventory();
            }
        });

        leftPanel.add(addByFileButton);

        // Making empty space between buttons
        leftPanel.add(Box.createRigidArea(new Dimension(10, 30)));

        removeByFileButton = new JButton("Remove With File");
        removeByFileButton.setFont(mediumFont);
        removeByFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeByFileButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        leftPanel.add(removeByFileButton);

        add(leftPanel, BorderLayout.WEST);

        // RIGHT INVENTORY PANEL

        // Initializing box that holds inventory panels
        inventoryBox = Box.createVerticalBox();
        // Initializing scrollable pane of inventory panels
        inventoryPanel = new JScrollPane(inventoryBox);
        inventoryPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        add(inventoryPanel, BorderLayout.CENTER);

        inventoryCards = new ArrayList<>();

        inventoryCards.add(new Card("Epic Card", 5.99f, 6.99f,"5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery"));

        inventoryCards.add(new Card("Lame Card", 5.99f, 6.99f,"5BB", "Return all nonland permanents \n " +
                "to their owner's hands.", "Ravnica", "Sorcery"));

        updateInventory();


    }

    public void updateInventory() {

        // Adds cards to scrollable pane for each card found
        for (Card card : inventoryCards) {

            // Creating new search panel entry
            InventoryEntryPanel inventoryPanel = new InventoryEntryPanel(card, 10, tabbedPane, searchUI);
            // Adding entry to search result box
            inventoryBox.add(inventoryPanel);

        }

        // Updating scrollable pane
        inventoryPanel.updateUI();
    }
}
