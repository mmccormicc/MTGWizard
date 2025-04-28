package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.domain.model.Card;
import org.capstone.mtgwizard.domain.service.InventoryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.*;

public class InventoryTab extends JPanel {

    InventoryService inventoryService;

    // Left panel components
    private JPanel leftPanel;
    // Holds drop down menu and buttons
    private JPanel inventoryEditPanel;


    // Right panel components
    private JPanel rightPanel;

    private Box inventoryBox;
    private JScrollPane inventoryScrollPanel;

    private ArrayList<Card> inventoryCards;

    private JTabbedPane tabbedPane;
    private SearchTab searchTab;

    public InventoryTab(JTabbedPane tabbedPane, SearchTab searchTab, InventoryService inventoryService) {

        this.inventoryService = inventoryService;

        // Setting tabbed pane so it can be passed
        this.tabbedPane = tabbedPane;
        // Setting search ui so it can be passed
        this.searchTab = searchTab;

        // Setting layout
        setLayout(new BorderLayout());

        // LEFT BUTTONS PANEL
        // This will hold inventory control panel, and a horizontal spacer
        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout());

        inventoryEditPanel = new JPanel();
        inventoryEditPanel.setLayout(new BoxLayout(inventoryEditPanel, BoxLayout.Y_AXIS));

        // Drop down menu with inventory options
        String[] inventoryOptions = {"Inventory 1", "Inventory 2", "Inventory 3", "Inventory 4", "Inventory 5"};
        JComboBox inventorySelect = new JComboBox(inventoryOptions);
        inventorySelect.setFont(mediumFont);
        inventorySelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventorySelect.setMaximumSize(new Dimension(200, 50));
        // Action listener when option is chosen
        inventorySelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {;
                // Updating inventory number to selection
                inventoryService.setInventory(inventorySelect.getSelectedIndex());
                updateInventory();
            }
        });
        inventoryEditPanel.add(inventorySelect);

        // Making empty space drop down menu and buttons
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 30)));

        // Add by file button
        JButton addByFileButton = new JButton("Add By File");
        addByFileButton.setFont(mediumFont);
        addByFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Action listener when button is clicked
        addByFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventoryService.addByFile();
                inventoryService.saveInventories();
                updateInventory();
            }
        });
        inventoryEditPanel.add(addByFileButton);

        // Making empty space between buttons
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 10)));

        // Remove by file button
        JButton removeByFileButton = new JButton("Remove By File");
        removeByFileButton.setFont(mediumFont);
        removeByFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Action listener when button is clicked
        removeByFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventoryService.removeByFile();
                inventoryService.saveInventories();
                updateInventory();
            }
        });
        inventoryEditPanel.add(removeByFileButton);

        // Making empty space between buttons
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 50)));


        // Clear Inventory Button
        JButton clearInventoryButton = new JButton("Clear Inventory");
        clearInventoryButton.setFont(mediumFont);
        clearInventoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Action listener when button is clicked
        clearInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Confirmation dialogue
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to remove all inventory entries?", // Message to display
                        "Confirmation", // Title of the dialog
                        JOptionPane.YES_NO_OPTION, // Option type (Yes/No)
                        JOptionPane.QUESTION_MESSAGE // Message type (Question)
                );

                // If user clicked yes in dialogue
                if (result == JOptionPane.YES_OPTION) {
                    inventoryService.clearCurrentInventory();
                    inventoryService.saveInventories();
                    updateInventory();
                }

            }
        });
        inventoryEditPanel.add(clearInventoryButton);

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
        inventoryScrollPanel = new JScrollPane(inventoryBox);
        inventoryScrollPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        // Getting vertical scroll bar and increasing scroll speed
        JScrollBar verticalScrollBar = inventoryScrollPanel.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(verticalScrollBar.getUnitIncrement() * 10);


        rightPanel.add(inventoryScrollPanel);

        // Right panel fills rest of space in frame
        add(rightPanel, BorderLayout.CENTER);

        // Testing adding cards to inventory
        inventoryCards = new ArrayList<>();
        inventoryCards.add(new Card("Epic Card", 5.99f, 6.99f,"5BB", "Return all nonland permanents " +
                "to their owner's hands.", "Ravnica", "Sorcery", "1"));

        inventoryCards.add(new Card("Lame Card", 5.99f, 6.99f,"5BB", "Return all nonland permanents \n " +
                "to their owner's hands.", "Ravnica", "Sorcery", "1"));

        updateInventory();


    }


    public void updateInventory() {

        inventoryBox.removeAll();

        for (Card card : inventoryService.selectedInventory.getCards()) {
            // Creating new search panel entry
            InventoryEntryPanel inventoryPanel = new InventoryEntryPanel(card, inventoryService.selectedInventory.getCardAmount(card), tabbedPane, searchTab);
            // Adding entry to search result box
            inventoryBox.add(inventoryPanel);
        }

        // Updating scrollable pane
        inventoryScrollPanel.updateUI();
    }

}
