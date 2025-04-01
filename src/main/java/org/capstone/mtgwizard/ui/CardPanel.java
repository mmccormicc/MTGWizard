package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;

public class CardPanel extends JPanel {

    Card card;

    CardPanel(Card card) {
        this.card = card;
        JLabel label = new JLabel(card.getName());
        add(label);
    }
}
