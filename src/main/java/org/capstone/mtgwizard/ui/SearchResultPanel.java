package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.domain.model.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.capstone.mtgwizard.ui.ProgramFonts.boldMediumFont;
import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class SearchResultPanel extends JPanel {

    // Holds card that panel represents
    private Card card;

    // Holds search tab that panel is part of
    SearchTab searchTab;

    SearchResultPanel(Card card, SearchTab searchTab) {

        this.card = card;
        this.searchTab = searchTab;

        // Making border around search result panel
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

        // Mouse listener for when panel is clicked on
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                searchTab.displayCardInfo(card);
            }
        });
    }

}
