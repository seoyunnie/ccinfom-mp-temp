package seoyunnie.dbapp.gui;

import java.awt.GridLayout;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AircraftInputDialog extends JPanel {
    private static final int TEXT_FIELD_LENGTH = 10;

    private final JTextField registrationInputField = new JTextField(TEXT_FIELD_LENGTH);
    private final JTextField modelInputField = new JTextField(TEXT_FIELD_LENGTH);

    public AircraftInputDialog() {
        setLayout(new GridLayout(2, 2, 5, 5));

        add(new JLabel("Registration Mark"));
        add(registrationInputField);

        add(new JLabel("Model"));
        add(modelInputField);
    }

    public Optional<String> getRegistration() {
        return Optional.ofNullable(registrationInputField.getText()).filter(Predicate.not(String::isEmpty));
    }

    public Optional<String> getModel() {
        return Optional.ofNullable(modelInputField.getText()).filter(Predicate.not(String::isEmpty));
    }
}
