package seoyunnie.dbapp.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import seoyunnie.dbapp.model.Hanger;
import seoyunnie.dbapp.model.MaintenancePeriod;
import seoyunnie.dbapp.service.AircraftService;
import seoyunnie.dbapp.service.MaintenanceService;
import seoyunnie.dbapp.service.ReplacementPartService;

public class HangerInfoFrame extends JFrame {
    private static final int TEXT_FIELD_LENGTH = 20;

    private final ListPanel<MaintenancePeriod> maintenanceHistoryList = new ListPanel<>();

    private final Hanger hanger;

    private final MaintenanceService maintenanceService;
    private final AircraftService aircraftService;
    private final ReplacementPartService replacementPartService;

    public HangerInfoFrame(MaintenanceService maintenanceService, AircraftService aircraftService,
            ReplacementPartService replacementPartService, Component parentComp, Hanger hanger) {
        super("Hanger Info");

        this.hanger = hanger;

        this.maintenanceService = maintenanceService;
        this.aircraftService = aircraftService;
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

        var idField = new JTextField(Integer.toString(hanger.getId()), TEXT_FIELD_LENGTH);
        idField.setEditable(false);

        infoPanel.add(idField);

        infoPanel.add(new JLabel("Location"));

        var locationField = new JTextField(hanger.getLocation(), TEXT_FIELD_LENGTH);
        locationField.setEditable(false);

        infoPanel.add(locationField);

        infoPanel.add(new JLabel("Status"));

        var statusField = new JTextField(hanger.getStatus().toString().toUpperCase(), TEXT_FIELD_LENGTH);
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

    private void addComponentListeners() {
        maintenanceHistoryList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    maintenanceHistoryList.getSelectedValue().ifPresent((p) -> new MaintenanceInfoFrame(
                            replacementPartService,
                            HangerInfoFrame.this,
                            p, aircraftService.getByRegistration(p.getAircraftRegistration()).get(), hanger));
                }
            }
        });
    }

    private void initializeListElements() {
        for (MaintenancePeriod period : maintenanceService.getAllByHanger(hanger)) {
            maintenanceHistoryList.addElement(period);
        }
    }
}
