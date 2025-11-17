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
import java.io.File;
import java.time.LocalDate;
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

import seoyunnie.dbapp.gui.dialog.MaintenanceInputDialog;
import seoyunnie.dbapp.model.Aircraft;
import seoyunnie.dbapp.model.AircraftCapacity;
import seoyunnie.dbapp.model.MaintenancePeriod;
import seoyunnie.dbapp.service.HangerService;
import seoyunnie.dbapp.service.MaintenanceService;
import seoyunnie.dbapp.service.ReplacementPartService;

public class AircraftInfoFrame extends JFrame {
    private final ListPanel<MaintenancePeriod> maintenanceHistoryList = new ListPanel<>();
    private final JButton scheduleMaintenanceButton = new JButton("Schedule Maintenance");
    private final JButton returnAircraftButton = new JButton("Return Aircraft");

    private final JButton createMaintenanceReportButton = new JButton("Create Maintenance Report");

    private final Aircraft aircraft;
    private final AircraftCapacity aircraftCapacity;

    private final MaintenanceService maintenanceService;
    private final HangerService hangerService;
    private final ReplacementPartService replacementPartService;

    public AircraftInfoFrame(MaintenanceService maintenanceService, HangerService hangerService,
            ReplacementPartService replacementPartService, Component parentComp, Aircraft aircraft,
            AircraftCapacity aircraftCapacity) {
        super("Aircraft Info");

        this.aircraft = aircraft;
        this.aircraftCapacity = aircraftCapacity;

        this.maintenanceService = maintenanceService;
        this.hangerService = hangerService;
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

        infoPanel.add(new JLabel("Registration Mark"));

        var registrationField = new JTextField(aircraft.getRegistration(), 10);
        registrationField.setEditable(false);

        infoPanel.add(registrationField);

        infoPanel.add(new JLabel("Model"));

        var modelField = new JTextField(aircraft.getModel(), 10);
        modelField.setEditable(false);

        infoPanel.add(modelField);

        infoPanel.add(new JLabel("Passenger Capacity"));

        var capacityField = new JTextField(Integer.toString(aircraftCapacity.getCapacity()), 10);
        capacityField.setEditable(false);

        infoPanel.add(capacityField);

        infoPanel.add(new JLabel("Status"));

        var statusField = new JTextField(aircraft.getStatus().toString().toUpperCase(), 10);
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
        constraints.insets.bottom = 5;

        maintenancePanel.add(maintenanceHistoryList, constraints);

        constraints.gridy++;
        constraints.insets.top = 5;

        maintenancePanel.add(scheduleMaintenanceButton, constraints);

        constraints.gridy++;
        constraints.insets.bottom = 10;

        maintenancePanel.add(returnAircraftButton, constraints);

        constraints.gridy++;
        constraints.insets.top = 0;
        constraints.insets.bottom = 0;

        maintenancePanel.add(new JSeparator(JSeparator.HORIZONTAL), constraints);

        constraints.gridy++;
        constraints.insets.top = 10;
        constraints.insets.bottom = 10;

        maintenancePanel.add(createMaintenanceReportButton, constraints);

        add(maintenancePanel, BorderLayout.PAGE_END);
    }

    private void addComponentListeners() {
        maintenanceHistoryList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    maintenanceHistoryList.getSelectedValue().ifPresent((p) -> new MaintenanceInfoFrame(
                            replacementPartService,
                            AircraftInfoFrame.this,
                            p, aircraft));
                }
            }
        });

        scheduleMaintenanceButton.addActionListener((evt) -> {
            var maintenanceInDialog = new MaintenanceInputDialog(hangerService.getAllAvailable());

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    maintenanceInDialog, "Schedule Maintenance",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (confirmation == JOptionPane.OK_OPTION) {
                Optional<String> type = maintenanceInDialog.getType();

                if (!type.isPresent()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill up all input fields!", "Missing Input",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                var maintenancePeriod = new MaintenancePeriod(
                        type.get(),
                        aircraft.getRegistration(), maintenanceInDialog.getHanger().getId(),
                        MaintenancePeriod.Status.ONGOING, LocalDate.now());

                if (!maintenanceService.scheduleMaintenance(maintenancePeriod)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "The aircraft is already under maintenance.", "Aircraft Under Maintenance",
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }

                maintenanceHistoryList.addElement(maintenancePeriod);
            }
        });

        returnAircraftButton.addActionListener((evt) -> {
            Optional<MaintenancePeriod> currMaintenancePeriod = maintenanceService.getAllByAircraft(aircraft).stream()
                    .filter((p) -> p.getCompletedAt() == null).findFirst();

            currMaintenancePeriod.ifPresent((p) -> maintenanceService.completeMaintenance(p));

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Close this window to view changes?", "Data Outdated",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (confirmation == JOptionPane.OK_OPTION) {
                AircraftInfoFrame.this.dispose();
            }
        });

        createMaintenanceReportButton.addActionListener((evt) -> {
            maintenanceHistoryList.getSelectedValue().ifPresent((period) -> {
                Optional<File> file = maintenanceService.createReport(period);

                file.ifPresentOrElse(
                        (f) -> JOptionPane.showMessageDialog(
                                this,
                                "Report exported to: " + f.getAbsolutePath(), "Report Created",
                                JOptionPane.INFORMATION_MESSAGE),
                        () -> JOptionPane.showMessageDialog(
                                this,
                                "The report could not be created.", "Report Creation Failed",
                                JOptionPane.ERROR_MESSAGE));
            });
        });
    }

    private void initializeListElements() {
        for (MaintenancePeriod period : maintenanceService.getAllByAircraft(aircraft)) {
            maintenanceHistoryList.addElement(period);
        }
    }
}
