package org.capstone.mtgwizard.reference;

import javax.swing.JFileChooser;
import java.io.File;

public class FileSelector {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("No file selected.");
        }
    }
}