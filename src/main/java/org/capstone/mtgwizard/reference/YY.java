package org.capstone.mtgwizard.reference;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class YY extends JFrame {
    static String[] args;

    public YY() {
        setSize(160, 200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        int icnt= args.length==0 ? 5 : Integer.parseInt(args[0]);

        Box box= Box.createVerticalBox();
        for (int i=1; i<=icnt; i++) {
            JButton btn= new JButton("Button "+i);
            btn.setMaximumSize(new Dimension(150, 30));
            box.add(btn);
        }
        JScrollPane scroll= new JScrollPane(box);
        scroll.setPreferredSize(new Dimension(150, 100));
        add(scroll);
        setVisible(true);
    }

    public static void main(String... args) {
        YY.args= args;
        EventQueue.invokeLater(YY::new);
    }

}