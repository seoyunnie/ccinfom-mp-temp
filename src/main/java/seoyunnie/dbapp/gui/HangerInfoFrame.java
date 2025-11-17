package seoyunnie.dbapp.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import seoyunnie.dbapp.model.Hanger;
import seoyunnie.dbapp.model.MaintenancePeriod;
import seoyunnie.dbapp.service.MaintenanceService;

public class HangerInfoFrame extends JFrame {
    private final ListPanel<MaintenancePeriod> maintenanceHistoryList = new ListPanel<>();

    private final Hanger hanger;

    private final MaintenanceService maintenanceService;

    public HangerInfoFrame(MaintenanceService maintenanceService, Component parentComp, Hanger hanger) {
        super("Hanger Info");

        this.hanger = hanger;

        this.maintenanceService = maintenanceService;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeComponents();

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

        var idField = new JTextField(Integer.toString(hanger.getId()), 10);
        idField.setEditable(false);

        infoPanel.add(idField);

        infoPanel.add(new JLabel("Location"));

        var locationField = new JTextField(hanger.getLocation(), 10);
        locationField.setEditable(false);

        infoPanel.add(locationField);

        infoPanel.add(new JLabel("Status"));

        var statusField = new JTextField(hanger.getStatus().toString().toUpperCase(), 10);
        statusField.setEditable(false);

        infoPanel.add(statusField);

        add(infoPanel, BorderLayout.PAGE_START);

        add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);

        var maintenancePanel = new JPanel();
        maintenancePanel.setLayout(new GridBagLayout());

        var maintenanceHistoryLabel = new JLabel("Maintenance History");
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
        constraints.insets.bottom = 10;

        maintenancePanel.add(maintenanceHistoryList, constraints);

        add(maintenancePanel, BorderLayout.PAGE_END);
    }

    private void initializeListElements() {
        for (MaintenancePeriod period : maintenanceService.getAllByHanger(hanger)) {
            maintenanceHistoryList.addElement(period);
        }
    }
}
