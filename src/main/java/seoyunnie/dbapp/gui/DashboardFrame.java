package seoyunnie.dbapp.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import seoyunnie.dbapp.gui.dialog.AircraftInputDialog;
import seoyunnie.dbapp.model.Aircraft;
import seoyunnie.dbapp.model.AircraftCapacity;
import seoyunnie.dbapp.model.Hanger;
import seoyunnie.dbapp.service.AircraftService;
import seoyunnie.dbapp.service.HangerService;
import seoyunnie.dbapp.service.MaintenanceService;
import seoyunnie.dbapp.service.ReplacementPartService;

public class DashboardFrame extends JFrame {
    private final ListPanel<Aircraft> aircraftList = new ListPanel<>();
    private final JButton addAircraftButton = new JButton("Add Aircraft");

    private final ListPanel<Hanger> hangerList = new ListPanel<>();
    private final JButton addHangerButton = new JButton("Add Hanger");

    private final JButton createMaintenanceScheduleButton = new JButton("Create Maintenance Schedule");
    private final JButton createPartsInventoryButton = new JButton("Create Replacement Parts Inventory");

    private final AircraftService aircraftService;
    private final HangerService hangerService;
    private final MaintenanceService maintenanceService;
    private final ReplacementPartService replacementPartService;

    public DashboardFrame(AircraftService aircraftService, HangerService hangerService,
            MaintenanceService maintenanceService, ReplacementPartService replacementPartService) {
        super("Aircraft Maintenance Dashboard");

        this.aircraftService = aircraftService;
        this.hangerService = hangerService;
        this.maintenanceService = maintenanceService;
        this.replacementPartService = replacementPartService;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        initializeComponents();
        addComponentListeners();

        pack();
        setResizable(false);

        setLocationRelativeTo(null);
        setVisible(true);

        initializeListElements();
    }

    private void initializeComponents() {
        var dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridBagLayout());

        var fleetLabel = new JLabel("Fleet");
        fleetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fleetLabel.setFont(new Font("Arial", Font.BOLD, 15));

        var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(12, 20, 4, 20);

        dashboardPanel.add(fleetLabel, constraints);

        constraints.gridy++;
        constraints.insets.top = 3;
        constraints.insets.bottom = 5;

        dashboardPanel.add(aircraftList, constraints);

        constraints.gridy++;
        constraints.insets.top = 10;

        dashboardPanel.add(addAircraftButton, constraints);

        var hangersLabel = new JLabel("Hangers");
        hangersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hangersLabel.setFont(new Font("Arial", Font.BOLD, 15));

        constraints.gridx++;
        constraints.gridy = 0;
        constraints.insets = new Insets(12, 20, 4, 20);

        dashboardPanel.add(hangersLabel, constraints);

        constraints.gridy++;
        constraints.insets.top = 3;
        constraints.insets.bottom = 5;

        dashboardPanel.add(hangerList, constraints);

        constraints.gridy++;
        constraints.insets.top = 10;
        constraints.insets.bottom = 10;

        dashboardPanel.add(addHangerButton, constraints);

        add(dashboardPanel, BorderLayout.PAGE_START);

        add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.CENTER);

        var reportsPanel = new JPanel();
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reportsPanel.setLayout(new GridLayout(1, 0, 10, 10));

        reportsPanel.add(createMaintenanceScheduleButton);
        reportsPanel.add(createPartsInventoryButton);

        add(reportsPanel, BorderLayout.PAGE_END);
    }

    private void addComponentListeners() {
        aircraftList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    aircraftList.getSelectedValue().ifPresent((a) -> new AircraftInfoFrame(
                            maintenanceService, hangerService, replacementPartService,
                            DashboardFrame.this,
                            a, aircraftService.getCapacityByModel(a.getModel()).get()));
                }
            }
        });

        addAircraftButton.addActionListener((evt) -> {
            var aircraftInDialog = new AircraftInputDialog();

            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    aircraftInDialog, "Add Aircraft",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (confirmation == JOptionPane.OK_OPTION) {
                Optional<String> registration = aircraftInDialog.getRegistration();
                Optional<String> model = aircraftInDialog.getModel();

                if (!registration.isPresent() || !model.isPresent()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill up all input fields!", "Missing Input",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                if (aircraftService.getCapacityByModel(model.get()).isEmpty()) {
                    String raw_capacity = JOptionPane.showInputDialog(
                            this,
                            "Enter the passenger capacity of a " + model.get() + " aircraft:",
                            "Configure Aircraft Capacity",
                            JOptionPane.QUESTION_MESSAGE);

                    if (raw_capacity != null && !raw_capacity.isEmpty()) {
                        try {
                            int capacity = Integer.parseInt(raw_capacity);

                            aircraftService.addCapacity(new AircraftCapacity(model.get(), capacity));
                        } catch (NumberFormatException ignored) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Please enter a valid whole number!", "Invalid Input",
                                    JOptionPane.ERROR_MESSAGE);

                            return;
                        }
                    }
                }

                var aircraft = new Aircraft(registration.get(), model.get(), Aircraft.Status.IN_SERVICE);

                if (!aircraftService.add(aircraft)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "An aircraft with the same registration already exists.", "Taken Registration Mark",
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }

                aircraftList.addElement(aircraft);
            }
        });

        hangerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    hangerList.getSelectedValue().ifPresent((h) -> new HangerInfoFrame(
                            maintenanceService,
                            DashboardFrame.this,
                            h));
                }
            }
        });

        addHangerButton.addActionListener((evt) -> {
            String location = JOptionPane.showInputDialog(
                    this,
                    "Enter the location of the hanger:", "Add Hanger",
                    JOptionPane.QUESTION_MESSAGE);

            if (location != null && !location.isEmpty()) {
                var hanger = new Hanger(location, Hanger.Status.AVAILABLE);

                if (!hangerService.add(hanger)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "A hanger already exists on the same location.", "Occupied Hanger Location",
                            JOptionPane.WARNING_MESSAGE);

                    return;
                }

                hangerList.addElement(hanger);
            }
        });

        createMaintenanceScheduleButton.addActionListener((evt) -> {
            aircraftList.getSelectedValue().ifPresent((aircraft) -> {
                Optional<File> file = maintenanceService.createScheduleReport(aircraft);

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

        createPartsInventoryButton.addActionListener((evt) -> {
            aircraftList.getSelectedValue().ifPresent((aircraft) -> {
                Optional<File> file = replacementPartService.createInventory(aircraft);

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
        for (Aircraft aircraft : aircraftService.getAll()) {
            aircraftList.addElement(aircraft);
        }

        for (Hanger hanger : hangerService.getAll()) {
            hangerList.addElement(hanger);
        }
    }
}
