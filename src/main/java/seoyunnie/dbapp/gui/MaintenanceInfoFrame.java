package seoyunnie.dbapp.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import seoyunnie.dbapp.gui.dialog.ReplacementPartInputDialog;
import seoyunnie.dbapp.model.Aircraft;
import seoyunnie.dbapp.model.Hanger;
import seoyunnie.dbapp.model.MaintenancePeriod;
import seoyunnie.dbapp.model.ReplacementPart;
import seoyunnie.dbapp.service.ReplacementPartService;

public class MaintenanceInfoFrame extends JFrame {
    private static final int TEXT_FIELD_LENGTH = 20;

    private final ListPanel<ReplacementPart> replacementPartList = new ListPanel<>();
    private final JButton addReplacementPartsButton = new JButton("Add Replacement Parts");

    private final MaintenancePeriod period;
    private final Aircraft aircraft;
    private final Hanger hanger;

    private final ReplacementPartService replacementPartService;

    public MaintenanceInfoFrame(ReplacementPartService replacementPartService, Component parentComp,
            MaintenancePeriod period, Aircraft aircraft, Hanger hanger) {
        super("Maintenance Period Info");

        this.period = period;
        this.aircraft = aircraft;
        this.hanger = hanger;

        this.replacementPartService = replacementPartService;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();
        addComponentListeners();

        pack();
        setResizable(false);

        setLocationRelativeTo(parentComp);
        setVisible(true);

        initializeListElements();
    }

    private void initializeComponents() {
        var infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        infoPanel.setLayout(new GridLayout(0, 2, 10, 10));

        infoPanel.add(new JLabel("ID"));

        var idField = new JTextField(Integer.toString(period.getId()), TEXT_FIELD_LENGTH);
        idField.setEditable(false);

        infoPanel.add(idField);

        infoPanel.add(new JLabel("Maintenance Type"));

        var typeField = new JTextField(period.getType(), TEXT_FIELD_LENGTH);
        typeField.setEditable(false);

        infoPanel.add(typeField);

        infoPanel.add(new JLabel("Hanger Location"));

        var locationField = new JTextField(hanger.getLocation(), TEXT_FIELD_LENGTH);
        locationField.setEditable(false);

        infoPanel.add(locationField);

        infoPanel.add(new JLabel("Status"));

        var statusField = new JTextField(period.getStatus().toString().toUpperCase(), TEXT_FIELD_LENGTH);
        statusField.setEditable(false);

        infoPanel.add(statusField);

        infoPanel.add(new JLabel("Started at"));

        var startedAtField = new JTextField(period.getStartedAt().toString(), TEXT_FIELD_LENGTH);
        startedAtField.setEditable(false);

        infoPanel.add(startedAtField);

        infoPanel.add(new JLabel("Completed at"));

        var completedAt = period.getCompletedAt();

        var completedAtField = new JTextField(
                completedAt != null ? completedAt.toString() : "ONGOING",
                TEXT_FIELD_LENGTH);
        completedAtField.setEditable(false);

        infoPanel.add(completedAtField);

        add(infoPanel, BorderLayout.PAGE_START);

        add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);

        var maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new GridBagLayout());

        var maintenanceHistoryLabel = new JLabel("Replacement Parts");
        maintenanceHistoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        maintenanceHistoryLabel.setFont(new Font("Arial", Font.BOLD, 15));

        var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(12, 10, 4, 10);

        maintenancePanel.add(maintenanceHistoryLabel, constraints);

        constraints.gridy++;
        constraints.insets.top = 3;
        constraints.insets.bottom = 5;

        maintenancePanel.add(replacementPartList, constraints);

        if (period.getCompletedAt() != null) {
            addReplacementPartsButton.setEnabled(false);
        }

        constraints.gridy++;
        constraints.insets.top = 10;
        constraints.insets.bottom = 10;

        maintenancePanel.add(addReplacementPartsButton, constraints);

        add(maintenancePanel, BorderLayout.PAGE_END);
    }

    private void addComponentListeners() {
        addReplacementPartsButton.addActionListener((evt) -> {
            var replacementPartInDialog = new ReplacementPartInputDialog();

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    replacementPartInDialog, "Add Replacement Part",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (confirmation == JOptionPane.OK_OPTION) {
                Optional<Integer> num = replacementPartInDialog.getNumber();
                Optional<String> name = replacementPartInDialog.getPartName();
                Optional<Integer> amount = replacementPartInDialog.getAmount();

                if (!num.isPresent() || !name.isPresent() || !amount.isPresent()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill up all input fields!", "Missing Input",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                var replacementPart = new ReplacementPart(
                        num.get(), name.get(),
                        amount.get(),
                        aircraft.getRegistration(), period.getId());

                if (replacementPartService.add(replacementPart)) {
                    replacementPartList.addElement(replacementPart);

                    return;
                }

                confirmation = JOptionPane.showConfirmDialog(
                        this,
                        "Close this window to view changes?", "Data Outdated",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (confirmation == JOptionPane.OK_OPTION) {
                    MaintenanceInfoFrame.this.dispose();
                }
            }
        });
    }

    private void initializeListElements() {
        for (ReplacementPart part : replacementPartService.getAllByAircraft(aircraft)) {
            replacementPartList.addElement(part);
        }
    }
}
