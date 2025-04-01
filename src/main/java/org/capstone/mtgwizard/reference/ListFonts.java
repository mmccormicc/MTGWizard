package org.capstone.mtgwizard.reference;

import java.awt.GraphicsEnvironment;

public class ListFonts {
    public static void main(String[] args) {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            System.out.println(fontName);
        }
    }
}
