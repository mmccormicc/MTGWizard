package org.capstone.mtgwizard.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SearchBarExample extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultArea;
    private List<String> data;

    public SearchBarExample() {
        super("Search Bar Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        data = new ArrayList<>();
        data.add("Apple");
        data.add("Banana");
        data.add("Cherry");
        data.add("Date");
        data.add("Grape");

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        resultArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void performSearch() {
        String searchText = searchField.getText().toLowerCase();
        resultArea.setText("");
        for (String item : data) {
            if (item.toLowerCase().contains(searchText)) {
                resultArea.append(item + "\n");
            }
        }
        if (resultArea.getText().isEmpty()) {
            resultArea.append("No results found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SearchBarExample();
            }
        });
    }
}
