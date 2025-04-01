package org.capstone.mtgwizard.ui;

import org.capstone.mtgwizard.dataobjects.Card;

import javax.swing.*;
import java.awt.*;

import static org.capstone.mtgwizard.ui.ProgramFonts.boldMediumFont;
import static org.capstone.mtgwizard.ui.ProgramFonts.mediumFont;

public class SearchResultPanel extends JPanel {

    SearchResultPanel(Card card) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        //resultPane.setLayout(new BoxLayout(resultPane, BoxLayout.Y_AXIS));

        setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weightx = 1;
        cons.gridx = 0;

        JLabel nameLabel = new JLabel(card.getName());
        nameLabel.setFont(boldMediumFont);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel setLabel = new JLabel(card.getSet());
        setLabel.setFont(mediumFont);
        setLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(nameLabel, cons);
        add(setLabel, cons);
    }

}
