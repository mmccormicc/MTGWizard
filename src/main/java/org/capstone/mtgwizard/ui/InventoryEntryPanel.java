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
    Card card;

    // Holds quantity of card in database
    int quantity;

    InventoryEntryPanel(Card card, int quantity, JTabbedPane tabbedPane, SearchUI searchUI) {

        this.card = card;
        this.quantity = quantity;

        // Adding border
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
        // Adding namelabel
        add(nameLabel, cons);

        // Creating label for card set
        JLabel setLabel = new JLabel(card.getSet());
        setLabel.setFont(mediumFont);
        // Adding setlabel
        add(setLabel, cons);

        // Creating label for card quantity
        JLabel quantityLabel = new JLabel("Qt. " + Integer.toString(quantity));
        quantityLabel.setFont(mediumFont);
        cons.gridx = 1;
        add(quantityLabel, cons);

        // Mouse listener for when panel is clicked on
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Sending user to card in search tab
                tabbedPane.setSelectedIndex(0);
                searchUI.displayCardInfo(card);
            }
        });

    }

}