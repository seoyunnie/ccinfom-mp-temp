package seoyunnie.dbapp.gui.dialog;

import java.awt.GridLayout;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import seoyunnie.dbapp.validator.IntegerDocumentFilter;

public class ReplacementPartInputDialog extends JPanel {
    private static final int TEXT_FIELD_LENGTH = 10;

    private final JTextField numInField = new JTextField(TEXT_FIELD_LENGTH);
    private final JTextField nameInField = new JTextField(TEXT_FIELD_LENGTH);
    private final JTextField amountInField = new JTextField(TEXT_FIELD_LENGTH);

    public ReplacementPartInputDialog() {
        ((PlainDocument) numInField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());
        ((PlainDocument) amountInField.getDocument()).setDocumentFilter(new IntegerDocumentFilter());

        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Part Number"));
        add(numInField);

        add(new JLabel("Part Name"));
        add(nameInField);

        add(new JLabel("Amount"));
        add(amountInField);
    }

    public Optional<Integer> getNumber() {
        String numStr = numInField.getText();

        if (numStr == null || numStr.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(numStr));
    }

    public Optional<String> getPartName() {
        return Optional.ofNullable(nameInField.getText()).filter(Predicate.not(String::isEmpty));
    }

    public Optional<Integer> getAmount() {
        String amountStr = amountInField.getText();

        if (amountStr == null || amountStr.isEmpty()) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(amountStr));
        } catch (NumberFormatException e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }
}
