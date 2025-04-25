package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.domain.model.Card;
import org.capstone.mtgwizard.domain.model.Inventory;
import org.capstone.mtgwizard.domain.service.InventoryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.capstone.mtgwizard.ui.ProgramFonts.*;

public class CardInfoPanel extends JPanel {

    private JPanel leftPanel;
    private JPanel rightPanel;

    private Card card;
    private JLabel nameLabel;
    private JLabel typeLabel;
    private JTextArea rulesTextArea;
    private JLabel setLabel;
    private JLabel tcgPriceLabel;
    private JLabel cardKingdomPriceLabel;
    private JButton addToInventoryButton;
    private JButton removeFromInventoryButton;
    private JPanel manaCostPanel;
    private JLabel costLabel;

    private JLabel errorLabel;

    CardInfoPanel(InventoryService inventoryService) {

        // Box layout for panel
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));


        // Holds constraints for grid bag
        GridBagConstraints constraints = new GridBagConstraints();

        // Initializing grid bag layout
        //GridBagLayout layout = new GridBagLayout();
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());

        // Card Name
        nameLabel = new JLabel();
        nameLabel.setFont(boldMediumFont);
        // Setting position in grid layout
        constraints.gridx = 0;
        constraints.gridy = 0;
        // Adding to grid
        leftPanel.add(nameLabel, constraints);

        // Card Type
        typeLabel = new JLabel();
        typeLabel.setFont(mediumFont);
        constraints.gridx = 0;
        constraints.gridy = 1;
        leftPanel.add(typeLabel, constraints);

        // Separator between type and rules text
        constraints.gridx = 0;
        constraints.gridy = 2;
        JSeparator separator1 = new JSeparator();
        separator1.setPreferredSize(new Dimension(400, 1));
        constraints.insets = new Insets(0, 0, 20, 0);
        leftPanel.add(separator1, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);

        // Rules text
        rulesTextArea = new JTextArea();
        rulesTextArea.setFont(mediumFont);
        // Making it so you can't click and edit rules text
        rulesTextArea.setFocusable(false);
        // Removing rules text background
        //rulesTextArea.setOpaque(false);
        constraints.gridx = 0;
        constraints.gridy = 3;
        // Spacing between left and right columns of card info
        leftPanel.add(rulesTextArea, constraints);

        add(leftPanel);


        // RIGHT SIDE COMPONENTS
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());

        // Card Set
        setLabel = new JLabel();
        setLabel.setFont(mediumFont);
        constraints.gridx = 0;
        constraints.gridy = 0;
        rightPanel.add(setLabel, constraints);

        // Initializing mana cost label
        costLabel = new JLabel("Mana Cost: ");
        costLabel.setFont(mediumFont);

        // Initializing mana cost panel without icons
        manaCostPanel = new JPanel();
        manaCostPanel.setLayout(new FlowLayout());
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 20, 0);
        rightPanel.add(manaCostPanel, constraints);


        // Card price from TCGplayer
        tcgPriceLabel = new JLabel();
        tcgPriceLabel.setFont(mediumFont);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(0, 0, 5, 0);
        rightPanel.add(tcgPriceLabel, constraints);
        //constraints.insets = new Insets(0, 0, 0, 0);

        // Card price from Card Kingdom
        cardKingdomPriceLabel = new JLabel();
        cardKingdomPriceLabel.setFont(mediumFont);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.insets = new Insets(0, 0, 30, 0);
        rightPanel.add(cardKingdomPriceLabel, constraints);


        // Button to add card to inventory
        addToInventoryButton = new JButton("Add To Inventory");
        addToInventoryButton.setFont(mediumFont);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.insets = new Insets(0, 0, 5, 0);
        rightPanel.add(addToInventoryButton, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);

        addToInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inventoryService.selectedInventory.add(card, 1);
                // Resetting error label as inventory has been updated
                errorLabel.setText("");
            }
        });

        // Label to display error for card removal
        errorLabel = new JLabel();
        errorLabel.setFont(boldMediumFont);
        errorLabel.setForeground(Color.RED);


        // Button to remove card from inventory
        removeFromInventoryButton = new JButton("Remove From Inventory");
        removeFromInventoryButton.setFont(mediumFont);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.insets = new Insets(0, 0, 30, 0);
        rightPanel.add(removeFromInventoryButton, constraints);
        constraints.insets = new Insets(0, 0, 0, 0);

        removeFromInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    inventoryService.selectedInventory.remove(card, 1);
                // Catching exception if card removed when not in inventory
                } catch (Inventory.RemoveException error) {
                    errorLabel.setText(error.getMessage());
                }
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 6;
        rightPanel.add(errorLabel, constraints);

        add(rightPanel);



    }

    // Updating card shown by card panel
    public void setCard(Card card) {
        this.card = card;
        updateComponents();
    }

    // Updating components with card information
    private void updateComponents() {
        nameLabel.setText(card.getName());
        typeLabel.setText(card.getType());
        rulesTextArea.setText(card.getRulesText());
        setLabel.setText("Set: " + card.getSet());

        // Resetting error label when new card is displayed
        errorLabel.setText("");


        // If price = -1, set as not found
        if(card.getTCGPlayerPrice() < 0) {
            tcgPriceLabel.setText("TCGplayer Price: Not Found");
        } else {
            // Formatting to 2 decimal points for price
            tcgPriceLabel.setText("TCGplayer Price: $" + String.format("%.2f", card.getTCGPlayerPrice()));
        }

        // If price = -1, set as not found
        if(card.getCardKingdomPrice() < 0) {
            cardKingdomPriceLabel.setText("Card Kingdom Price: Not Found");
        } else {
            // Formatting to 2 decimal points for price
            cardKingdomPriceLabel.setText("Card Kingdom Price: $" + String.format("%.2f", card.getCardKingdomPrice()));
        }

        // Removing old mana cost
        manaCostPanel.removeAll();
        // Adding numeric cost
        manaCostPanel.add(costLabel);

        // Getting mana cost from card and storing it
        String manaCost = card.getManaCost();
        // For each character in mana cost
        for (int i = 0; i < manaCost.length(); i++) {
            // If char is an integer
            if(Character.isDigit(manaCost.charAt(i))) {
                // Creating label with cost int and adding to manaCost
                JLabel numberLabel = new JLabel(String.valueOf(manaCost.charAt(i)));
                numberLabel.setFont(boldLargeFont);
                manaCostPanel.add(numberLabel);
            } else {
                // If char is not an integer
                String imageName = "ErrorIcon.png";
                // Assigning image based on letter representing color
                switch (manaCost.charAt(i)) {
                    case 'W':
                        imageName = "WhiteIcon.png";
                        break;
                    case 'U':
                        imageName = "BlueIcon.png";
                        break;
                    case 'B':
                        imageName = "BlackIcon.png";
                        break;
                    case 'R':
                        imageName = "RedIcon.png";
                        break;
                    case 'G':
                        imageName = "GreenIcon.png";
                        break;
                }
                ImageIcon imageIcon = new ImageIcon("src/main/resources/images/" + imageName);
                JLabel imageLabel = new JLabel(imageIcon);
                manaCostPanel.add(imageLabel);
            }
        }

    }
}
