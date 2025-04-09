package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import java.awt.*;

import static org.capstone.mtgwizard.ui.ProgramFonts.*;

public class CardPanel extends JPanel {

    JPanel leftPanel;
    JPanel rightPanel;

    Card card;
    JLabel nameLabel;
    JLabel typeLabel;
    JTextArea rulesTextArea;
    JLabel setLabel;
    JLabel tcgPriceLabel;
    JLabel cardKingdomPriceLabel;
    JButton addToInventoryButton;
    JButton removeFromInventoryButton;
    JPanel manaCostPanel;
    JLabel costLabel;

    JPanel cardInfoPanel;

    CardPanel() {

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

        // Trying to set constant horizontal space between panels
//        Component spacerBox = Box.createRigidArea(new Dimension(100, 30));
//        spacerBox.setMaximumSize(new Dimension(100, 30));
//        add(Box.createHorizontalStrut(100));
 //         add(Box.createRigidArea(new Dimension(100, 0)));
//        Dimension minSize = new Dimension(5, 100);
//        Dimension prefSize = new Dimension(5, 100);
//        Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);
//        add(new Box.Filler(minSize, prefSize, maxSize));


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

        // Button to remove card from inventory
        removeFromInventoryButton = new JButton("Remove From Inventory");
        removeFromInventoryButton.setFont(mediumFont);
        constraints.gridx = 0;
        constraints.gridy = 5;
        rightPanel.add(removeFromInventoryButton, constraints);

        add(rightPanel);

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


        // If price = -1, set as not found
        if(card.getTCGPlayerPrice() < 0) {
            tcgPriceLabel.setText("TCGplayer Price: Not Found");
        } else {
            tcgPriceLabel.setText("TCGplayer Price: $" + Float.toString(card.getTCGPlayerPrice()));
        }

        // If price = -1, set as not found
        if(card.getCardKingdomPrice() < 0) {
            cardKingdomPriceLabel.setText("Card Kingdom Price: Not Found");
        } else {
            cardKingdomPriceLabel.setText("Card Kingdom Price: $" + Float.toString(card.getCardKingdomPrice()));
        }

        // Updating mana cost panel
        manaCostPanel.removeAll();
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
