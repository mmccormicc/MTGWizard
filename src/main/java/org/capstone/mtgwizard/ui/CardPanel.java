package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import static org.capstone.mtgwizard.ui.ProgramFonts.boldMediumFont;
import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends JPanel {

    Card card;
    JLabel nameLabel;
    JLabel typeLabel;
    JTextArea rulesTextArea;
    JLabel setLabel;
    JLabel tcgPriceLabel;
    JLabel cardKingdomPriceLabel;
    JButton addToInventoryButton;
    JButton removeFromInventoryButton;

    CardPanel() {

        // Holds constraints for gtrid bag
        GridBagConstraints constraints = new GridBagConstraints();
        // Constraints will fill out horizontal space
        constraints.weightx = 1;

        // Initializing grid bag layout
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        // LEFT SIDE COMPONENTS

        // Card Name
        nameLabel = new JLabel();
        nameLabel.setFont(boldMediumFont);
        // Setting position in grid layout
        constraints.gridx = 0;
        constraints.gridy = 0;
        // Adding to grid
        add(nameLabel, constraints);

        // Card Type
        typeLabel = new JLabel();
        typeLabel.setFont(mediumFont);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(typeLabel, constraints);

        // Separator between type and rules text
        constraints.gridx = 0;
        constraints.gridy = 2;
        JSeparator separator1 = new JSeparator();
        separator1.setPreferredSize(new Dimension(400, 20));
        add(separator1, constraints);
        add(separator1, constraints);

        // Rules text
        rulesTextArea = new JTextArea();
        rulesTextArea.setFont(mediumFont);
        // Making it so you can't click and edit rules text
        rulesTextArea.setFocusable(false);
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(rulesTextArea, constraints);

        // Testing card cost in middle
        JLabel testCost = new JLabel("R");
        testCost.setFont(mediumFont);
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(testCost, constraints);

        // RIGHT SIDE COMPONENTS

        // Card Set
        setLabel = new JLabel();
        setLabel.setFont(mediumFont);
        constraints.gridx = 2;
        constraints.gridy = 0;
        add(setLabel, constraints);

        // Separator between add to inventory and remove form inventory buttons
        constraints.gridx = 2;
        constraints.gridy = 1;
        JSeparator separator2 = new JSeparator();
        add(separator2, constraints);

        // Card price from TCGplayer
        tcgPriceLabel = new JLabel();
        tcgPriceLabel.setFont(mediumFont);
        constraints.gridx = 2;
        constraints.gridy = 2;
        add(tcgPriceLabel, constraints);

        // Card price from Card Kingdom
        cardKingdomPriceLabel = new JLabel();
        cardKingdomPriceLabel.setFont(mediumFont);
        constraints.gridx = 2;
        constraints.gridy = 3;
        add(cardKingdomPriceLabel, constraints);

        // Separator between price and add to inventory button
        constraints.gridx = 2;
        constraints.gridy = 4;
        JSeparator separator3 = new JSeparator();
        separator3.setPreferredSize(new Dimension(0, 20));
        add(separator3, constraints);

        // Button to add card to inventory
        addToInventoryButton = new JButton("Add To Inventory");
        addToInventoryButton.setFont(mediumFont);
        constraints.gridx = 2;
        constraints.gridy = 5;
        add(addToInventoryButton, constraints);

        // Button to remove card from inventory
        removeFromInventoryButton = new JButton("Remove From Inventory");
        removeFromInventoryButton.setFont(mediumFont);
        constraints.gridx = 2;
        constraints.gridy = 6;
        add(removeFromInventoryButton, constraints);

    }

    // Updating card shown by card panel
    public void setCard(Card card) {
        this.card = card;
        updateComponents();
    }

    // Updating components with card information
    public void updateComponents() {
        nameLabel.setText(card.getName());
        typeLabel.setText(card.getType());
        rulesTextArea.setText(card.getRulesText());
        setLabel.setText("Set: " + card.getSet());
        tcgPriceLabel.setText("TCGplayer Price: $" + Float.toString(card.getTCGPlayerPrice()));
        cardKingdomPriceLabel.setText("Card Kingdom Price: $" + Float.toString(card.getCardKingdomPrice()));
    }
}
