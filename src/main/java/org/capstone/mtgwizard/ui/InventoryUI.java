package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.*;

public class InventoryUI extends JPanel {
    // Left panel components
    private JPanel leftPanel;

    private JPanel inventoryEditPanel;
    private JComboBox inventorySelect;
    // Holds currently selected inventory
    private int inventoryNumber = 0;
    // Holds inventory options
    String[] inventoryOptions = {"Inventory 1", "Inventory 2", "Inventory 3", "Inventory 4", "Inventory 5"};

    private JButton addByFileButton;
    private JButton removeByFileButton;

    // Right panel components
    private JPanel rightPanel;

    private Box inventoryBox;
    private JScrollPane inventoryPanel;

    private ArrayList<Card> inventoryCards;

    private JTabbedPane tabbedPane;
    private SearchUI searchUI;

    InventoryUI(JTabbedPane tabbedPane, SearchUI searchUI) {
        // Setting tabbed pane so it can be passed
        this.tabbedPane = tabbedPane;
        // Setting search ui so it can be passed
        this.searchUI = searchUI;

        // Setting layout
        setLayout(new BorderLayout());

        // LEFT BUTTONS PANEL
        // This will hold inventory control panel, and a horizontal spacer

        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout());

        inventoryEditPanel = new JPanel();
        inventoryEditPanel.setLayout(new BoxLayout(inventoryEditPanel, BoxLayout.Y_AXIS));

        // Drop down menu with inventory options
        inventorySelect = new JComboBox(inventoryOptions);
        inventorySelect.setFont(mediumFont);
        inventorySelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventorySelect.setMaximumSize(new Dimension(200, 50));
        // Action listener when option is chosen
        inventorySelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(inventorySelect.getSelectedItem());
                System.out.println(inventorySelect.getSelectedIndex());
                // Updating inventory number to selection
                inventoryNumber = inventorySelect.getSelectedIndex();
            }
        });
        inventoryEditPanel.add(inventorySelect);

        // Making empty space drop down menu and buttons
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 30)));

        // Add by file button
        addByFileButton = new JButton("Add By File");
        addByFileButton.setFont(mediumFont);
        addByFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Action listener when button is clicked
        addByFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateInventory();
            }
        });
        inventoryEditPanel.add(addByFileButton);

        // Making empty space between buttons
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        // Remove by file button
        removeByFileButton = new JButton("Remove By File");
        removeByFileButton.setFont(mediumFont);
        removeByFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Action listener when button is clicked
        removeByFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        inventoryEditPanel.add(removeByFileButton);

        // Adding inventory edit panel
        leftPanel.add(inventoryEditPanel);
        // Adding horizontal spacer
        leftPanel.add(Box.createRigidArea(new Dimension(50, 30)));
        // Adding left panel to left of frame
        add(leftPanel, BorderLayout.WEST);

        // RIGHT INVENTORY PANEL

        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // Inventory label
        JLabel inventoryLabel = new JLabel("Inventory");
        inventoryLabel.setFont(boldMediumFont);
        inventoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(inventoryLabel);

        // Initializing box that holds inventory panels
        inventoryBox = Box.createVerticalBox();
        // Initializing scrollable pane of inventory panels
        inventoryPanel = new JScrollPane(inventoryBox);
        inventoryPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        rightPanel.add(inventoryPanel);

        // Right panel fills rest of space in frame
        add(rightPanel, BorderLayout.CENTER);

        // Testing adding cards to inventory
        inventoryCards = new ArrayList<>();
        inventoryCards.add(new Card("Epic Card", 5.99f, 6.99f,"5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery"));

        inventoryCards.add(new Card("Lame Card", 5.99f, 6.99f,"5BB", "Return all nonland permanents \n " +
                "to their owner's hands.", "Ravnica", "Sorcery"));

        updateInventory();


    }

    public int getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(int inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
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
