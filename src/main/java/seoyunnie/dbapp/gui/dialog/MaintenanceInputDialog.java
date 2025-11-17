package seoyunnie.dbapp.gui.dialog;

import java.awt.GridLayout;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import seoyunnie.dbapp.model.Hanger;

public class MaintenanceInputDialog extends JPanel {
    private static final int TEXT_FIELD_LENGTH = 10;

    private final JTextField typeInField = new JTextField(TEXT_FIELD_LENGTH);
    private final JComboBox<Hanger> hangerComboBox;

    public MaintenanceInputDialog(List<Hanger> hangers) {
        this.hangerComboBox = new JComboBox<>(hangers.toArray(Hanger[]::new));
        hangerComboBox.setPreferredSize(typeInField.getPreferredSize());

        setLayout(new GridLayout(2, 2, 5, 5));

        add(new JLabel("Type of Maintenance"));
        add(typeInField);

        add(new JLabel("Hanger"));
        add(hangerComboBox);
    }

    public Optional<String> getType() {
        return Optional.ofNullable(typeInField.getText()).filter(Predicate.not(String::isEmpty));
    }

    public Hanger getHanger() {
        return (Hanger) hangerComboBox.getSelectedItem();
    }
}
