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

import seoyunnie.dbapp.model.Aircraft;
import seoyunnie.dbapp.model.AircraftCapacity;

public class AircraftInfoPanel extends JFrame {
    public AircraftInfoPanel(Component parentComp, Aircraft aircraft, AircraftCapacity aircraftCapacity) {
        super("Aircraft Info");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        var infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        infoPanel.setLayout(new GridLayout(0, 2, 10, 10));

        infoPanel.add(new JLabel("Registration Mark"));

        var registrationField = new JTextField(aircraft.getRegistration());
        registrationField.setEditable(false);
        registrationField.setFocusable(false);

        infoPanel.add(registrationField);

        infoPanel.add(new JLabel("Model"));

        var modelField = new JTextField(aircraft.getModel());
        modelField.setEditable(false);
        modelField.setFocusable(false);

        infoPanel.add(modelField);

        infoPanel.add(new JLabel("Capacity"));

        var capacityField = new JTextField(Integer.toString(aircraftCapacity.getCapacity()));
        capacityField.setEditable(false);
        capacityField.setFocusable(false);

        infoPanel.add(capacityField);

        infoPanel.add(new JLabel("Status"));

        var statusField = new JTextField(aircraft.getStatus().toString().toUpperCase(Locale.US));
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
