package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.domain.model.Card;
import org.capstone.mtgwizard.domain.service.InventoryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import static org.capstone.mtgwizard.ui.ProgramFonts.*;

public class InventoryTab extends JPanel {

    InventoryService inventoryService;

    // Left panel components
    private JPanel leftPanel;
    // Holds drop down menu and buttons
    private JPanel inventoryEditPanel;

    // Holds help dialogue
    private JDialog helpDialogue;

    // Right panel components
    private JPanel rightPanel;

    // Inventory scroll pane vars
    private Box inventoryBox;
    private JScrollPane inventoryScrollPanel;

    // Cards currently being displayed in inventory
    private ArrayList<Card> inventoryCards;

    // Holds tabbed pane
    private JTabbedPane tabbedPane;
    // Holds search tab
    private SearchTab searchTab;

    private JComboBox<String> inventorySelect;

    public InventoryTab(JTabbedPane tabbedPane, SearchTab searchTab, InventoryService inventoryService) {

        // Handles adding and removing cards from inventory, as well as saving and loading inventories
        this.inventoryService = inventoryService;

        // Setting tabbed pane so it can be passed
        this.tabbedPane = tabbedPane;
        // Setting search ui so it can be passed
        this.searchTab = searchTab;

        // Setting layout
        setLayout(new BorderLayout());

        /////////////////////
        // LEFT BUTTONS PANEL
        /////////////////////

        // This holds inventory edit buttons panel, and spacer between scrollable card panel
        leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout());

        // This panel holds components to select and edit inventory
        inventoryEditPanel = new JPanel();
        inventoryEditPanel.setLayout(new BoxLayout(inventoryEditPanel, BoxLayout.Y_AXIS));


        // Creating help dialogue but not displaying it yet
        helpDialogue = createHelpDialog();

        // Initializing help button
        JButton helpButton = new JButton("Help");
        helpButton.setFont(mediumFont);
        helpButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        // Adding action listener for when help button is clicked
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Starting dialogue in separate thread so main window can still be interacted with
                SwingUtilities.invokeLater(() -> {
                    helpDialogue.setVisible(true);
                });
            }
        });
        inventoryEditPanel.add(helpButton);


        // Making empty space between help button and 'Select Inventory' label
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 60)));


        // Select inventory label
        JLabel selectInventoryLabel = new JLabel("Select Inventory");
        selectInventoryLabel.setFont(mediumFont);
        selectInventoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventoryEditPanel.add(selectInventoryLabel);

        // Making empty space between label and menu
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 5)));

        // Drop down menu with inventory options (now populated dynamically)
        inventorySelect = new JComboBox<>(inventoryService.getInventoryNames());
        inventorySelect.setFont(mediumFont);
        inventorySelect.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventorySelect.setMaximumSize(new Dimension(200, 50));
        // Set initial selection
        inventorySelect.setSelectedIndex(inventoryService.inventorySelectedIndex);
        // Action listener when option is chosen
        inventorySelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Updating inventory number to selection
                int selectedIndex = inventorySelect.getSelectedIndex();
                inventoryService.setInventory(selectedIndex);
                updateInventory();
            }
        });
        inventoryEditPanel.add(inventorySelect);

        // Making empty space between inventory dropdown and create inventory button
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 5)));

        // Create Inventory button
        JButton createInventoryButton = new JButton("Create Inventory");
        createInventoryButton.setFont(mediumFont);
        createInventoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt for inventory name
                String inventoryName = JOptionPane.showInputDialog(
                        null,
                        "Enter a name for the new inventory:",
                        "Create New Inventory",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (inventoryName != null && !inventoryName.trim().isEmpty()) {
                    try {

                        int oldIndex = inventoryService.inventorySelectedIndex;

                        // Create the new inventory
                        int newIndex = inventoryService.createNewInventory(
                                inventoryName,
                                inventoryService.getDataDirectory()
                        );

                        //  If new inventory was actually created
                        if (newIndex != oldIndex) {
                            // Update the combo box
                            inventorySelect.addItem(inventoryName);
                            // Select the new inventory
                            inventorySelect.setSelectedIndex(newIndex);
                            // Update service and UI
                            inventoryService.setInventory(newIndex);
                            updateInventory();
                            // Save changes
                            inventoryService.saveInventories();
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Error creating inventory: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } else {
                    // Error message for creating inventory with empty name
                    JOptionPane.showMessageDialog(
                            null,
                            "Error creating inventory: Inventory name can't be empty.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        inventoryEditPanel.add(createInventoryButton);

        // Making empty space between buttons and next section
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 20)));


        // Mass entry label
        JLabel massEntryLabel = new JLabel("Edit Inventory");
        massEntryLabel.setFont(mediumFont);
        massEntryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventoryEditPanel.add(massEntryLabel);

        // Making empty space between label and buttons
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 5)));

        // Add by file button
        JButton addByFileButton = new JButton("Add By File");
        addByFileButton.setFont(mediumFont);
        addByFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Action listener when button is clicked
        addByFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    String errorString = inventoryService.addByFile();
                    inventoryService.saveInventories();
                    updateInventory();

                    if (errorString != "") {

                        // Showing lines that produced errors
                        JOptionPane.showMessageDialog(null,
                                "<html><font face='Arial' size='4' color='black'>" +
                                        errorString + "<br>" +
                                        "These cards were not added to inventory." +
                                        "</font></html>",
                                "Inventory Add Errors",
                                JOptionPane.ERROR_MESSAGE
                        );

                    }
                } catch (InventoryService.FileTypeException fileTypeException) {
                    JOptionPane.showMessageDialog(null,
                            "<html><font face='Arial' size='4' color='black'>" +
                                    fileTypeException.getMessage() +
                                    "</font></html>",
                            "File Type Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        inventoryEditPanel.add(addByFileButton);

        // Making empty space between buttons
        inventoryEditPanel.add(Box.createRigidArea(new Dimension(10, 5)));

        // Remove by file button
        JButton removeByFileButton = new JButton("Remove By File");
        removeByFileButton.setFont(mediumFont);
        removeByFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Action listener when button is clicked
        removeByFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String errorString = inventoryService.removeByFile();
                    inventoryService.saveInventories();
                    updateInventory();

                    if (errorString != "") {

                        // Showing lines that produced errors
                        JOptionPane.showMessageDialog(null,
                                "<html><font face='Arial' size='4' color='black'>" +
                                        errorString + "<br>" +
                                        "These cards were not removed from inventory." +
                                        "</font></html>",
                                "Inventory Remove Errors",
                                JOptionPane.ERROR_MESSAGE
                        );

                    }

                } catch (InventoryService.FileTypeException fileTypeException) {
                    JOptionPane.showMessageDialog(null,
                            "<html><font face='Arial' size='4' color='black'>" +
                                    fileTypeException.getMessage() +
                                    "</font></html>",
                            "File Type Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
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

    // Create the JDialog for help window
    private static JDialog createHelpDialog() {
        JDialog dialog = new JDialog(null, "Inventory Help", Dialog.ModalityType.MODELESS); // Modeless tells dialogue to not block interactions with other windows

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(800, 450);
        dialog.setLocationRelativeTo(null);

        // Message to be displayed
        String message = "<html><font face='Arial' size='4' color='black'>" +
                "This is the inventory tab. Here you can keep track of lists of cards that will be saved between sessions.<br>" +
                "This is useful for creating decks, or cataloging store inventory.<br><br>" +

                "<font color='blue'>Select Inventory</font><br>" +
                "This section lets you choose between saved inventories, and create new ones.<br>" +
                "Select the inventory you want to edit from the drop down menu.<br>" +
                "Create new inventories with custom names with the 'Create Inventory' button.<br><br>" +

                "<font color='blue'>Edit Inventory</font><br>" +
                "These buttons allow you to mass edit inventories by uploading files.<br>" +
                "Upload a .txt file with a list of cards shown in the format below<br>" +
                "Each line should include a card and optionally include quantity.<br><br>" +

                "<font color='red'>" +
                "name: sol ring set: C13<br>" +
                "Lightning Bolt 2<br>" +
                "Island 10<br>" +
                "</font><br><br>"+

                "Make sure to put card quantity at the end of the line.<br>" +
                "The first card found matching the line will be added to inventory.<br><br>" +

                "<font color='blue'>Clear Inventory</font><br>" +
                "This button removes all cards from the selected inventory.<br><br>" +

                "</font></html>";

        // Creating label with message
        JLabel label = new JLabel(message);
        // Centering label
        label.setHorizontalAlignment(SwingConstants.CENTER);

        dialog.add(label);

        return dialog;
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
