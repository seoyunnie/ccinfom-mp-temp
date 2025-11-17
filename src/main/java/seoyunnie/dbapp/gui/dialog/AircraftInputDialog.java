package seoyunnie.dbapp.gui.dialog;

import java.awt.GridLayout;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AircraftInputDialog extends JPanel {
    private static final int TEXT_FIELD_LENGTH = 10;

    private final JTextField registrationInField = new JTextField(TEXT_FIELD_LENGTH);
    private final JTextField modelInField = new JTextField(TEXT_FIELD_LENGTH);

    public AircraftInputDialog() {
        setLayout(new GridLayout(2, 2, 5, 5));

        add(new JLabel("Registration Mark"));
        add(registrationInField);

        add(new JLabel("Model"));
        add(modelInField);
    }

    public Optional<String> getRegistration() {
        return Optional.ofNullable(registrationInField.getText()).filter(Predicate.not(String::isEmpty));
    }

    public Optional<String> getModel() {
        return Optional.ofNullable(modelInField.getText()).filter(Predicate.not(String::isEmpty));
    }
}
