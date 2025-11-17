package seoyunnie.dbapp.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import seoyunnie.dbapp.model.Hanger;

public class HangerInfoPanel extends JFrame {
    public HangerInfoPanel(Component parentComp, Hanger hanger) {
        super("Hanger Info");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        var infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        infoPanel.setLayout(new GridLayout(0, 2, 10, 10));

        infoPanel.add(new JLabel("ID"));

        var idField = new JTextField(Integer.toString(hanger.getId()));
        idField.setEditable(false);
        idField.setFocusable(false);

        infoPanel.add(idField);

        infoPanel.add(new JLabel("Location"));

        var locationField = new JTextField(hanger.getLocation());
        locationField.setEditable(false);
        locationField.setFocusable(false);

        infoPanel.add(locationField);

        infoPanel.add(new JLabel("Status"));

        var statusField = new JTextField(hanger.getStatus().toString().toUpperCase(Locale.US));
        statusField.setEditable(false);
        statusField.setFocusable(false);

        infoPanel.add(statusField);

        add(infoPanel, BorderLayout.PAGE_START);

        add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);

        pack();

        setLocationRelativeTo(parentComp);
        setVisible(true);
    }
}
