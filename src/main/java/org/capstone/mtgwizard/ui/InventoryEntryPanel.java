package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.capstone.mtgwizard.ui.ProgramFonts.boldMediumFont;
import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class InventoryEntryPanel extends JPanel {

    // Holds card that panel represents
    Card card;;

    InventoryEntryPanel(Card card) {

        this.card = card;

        setBorder(BorderFactory.createLineBorder(Color.black));

        // Setting layout
        setLayout(new GridBagLayout());
        // Creating constraints that make panel fill out window horizontally
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weightx = 1;
        cons.gridx = 0;

        // Creating label for card name
        JLabel nameLabel = new JLabel(card.getName());
        nameLabel.setFont(boldMediumFont);

        // Creating label for card set
        JLabel setLabel = new JLabel(card.getSet());
        setLabel.setFont(mediumFont);

        // Adding labels to panel
        add(nameLabel, cons);
        add(setLabel, cons);

    }

}