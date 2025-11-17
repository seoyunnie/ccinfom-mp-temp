package seoyunnie.dbapp.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import seoyunnie.dbapp.model.Aircraft;
import seoyunnie.dbapp.model.AircraftCapacity;
import seoyunnie.dbapp.model.Hanger;
import seoyunnie.dbapp.service.AircraftService;
import seoyunnie.dbapp.service.HangerService;

public class DashboardFrame extends JFrame {
    private final ListPanel aircraftList = new ListPanel();
    private final JButton addAircraftButton = new JButton("Add Aircraft");
    private final JButton removeAircraftButton = new JButton("Remove Aircraft");

    private final ListPanel hangerList = new ListPanel();
    private final JButton addHangerButton = new JButton("Add Hanger");
    private final JButton removeHangerButton = new JButton("Remove Hanger");

    private final AircraftService aircraftService;
    private final HangerService hangerService;

    public DashboardFrame(AircraftService aircraftService, HangerService hangerService) {
        super("Aircraft Maintenance Dashboard");

        this.aircraftService = aircraftService;
        this.hangerService = hangerService;

        aircraftList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    aircraftList.getTrimmedSelectedValue().ifPresent((r) -> {
                        Aircraft aircraft = aircraftService.getByRegistration(r).get();

                        new AircraftInfoPanel(
                                DashboardFrame.this,
                                aircraft, aircraftService.getCapacityByModel(aircraft.getModel()).get());
                    });
                }
            }
        });

        addAircraftButton.addActionListener((evt) -> {
            var aircraftInputDialog = new AircraftInputDialog();

            int res = JOptionPane.showConfirmDialog(
                    this,
                    aircraftInputDialog, "Add Aircraft",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            Optional<String> registration = aircraftInputDialog.getRegistration();
            Optional<String> model = aircraftInputDialog.getModel();

            if (res == JOptionPane.OK_OPTION && registration.isPresent() && model.isPresent()) {
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
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Please enter a valid whole number.", "Invalid Input",
                                    JOptionPane.ERROR_MESSAGE);
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

                aircraftList.addElement(aircraft.toString());
            }
        });

        removeAircraftButton.addActionListener((evt) -> {
            aircraftList.getTrimmedSelectedValue().ifPresent((r) -> aircraftService.removeByRegistration(r));
            aircraftList.getSelectedValue().ifPresent((r) -> aircraftList.removeElement(r));
        });

        hangerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    hangerList.getTrimmedSelectedValue().ifPresent((i) -> new HangerInfoPanel(
                            DashboardFrame.this,
                            hangerService.getById(Integer.parseInt(i)).get()));
                }
            }
        });

        addHangerButton.addActionListener((evt) -> {
            String location = JOptionPane.showInputDialog(
                    this,
                    "Enter the location of the hanger:", "Add Hanger",
                    JOptionPane.QUESTION_MESSAGE);

            var hanger = new Hanger(location, Hanger.Status.AVAILABLE);

            if (!hangerService.add(hanger)) {
                JOptionPane.showMessageDialog(
                        this,
                        "A hanger already exists on the same location.", "Occupied Hanger Location",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            hangerList.addElement(hanger.toString());
        });

        removeHangerButton.addActionListener((evt) -> {
            hangerList.getTrimmedSelectedValue().ifPresent((i) -> hangerService.removeById(Integer.parseInt(i)));
            hangerList.getSelectedValue().ifPresent((i) -> hangerList.removeElement(i));
        });

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        setLayout(new GridBagLayout());

        var fleetLabel = new JLabel("Fleet");
        fleetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fleetLabel.setFont(new Font("Arial", Font.BOLD, 15));

        var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(12, 10, 4, 10);

        add(fleetLabel, constraints);

        constraints.gridy++;
        constraints.insets.top = 3;
        constraints.insets.bottom = 5;

        add(aircraftList, constraints);

        constraints.gridy++;
        constraints.insets.top = 5;

        add(addAircraftButton, constraints);

        constraints.gridy++;
        constraints.insets.bottom = 10;

        add(removeAircraftButton, constraints);

        var hangersLabel = new JLabel("Hangers");
        hangersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hangersLabel.setFont(new Font("Arial", Font.BOLD, 15));

        constraints.gridx++;
        constraints.gridy = 0;
        constraints.insets = new Insets(12, 10, 4, 10);

        add(hangersLabel, constraints);

        constraints.gridy++;
        constraints.insets.top = 3;
        constraints.insets.bottom = 5;

        add(hangerList, constraints);

        constraints.gridy++;
        constraints.insets.top = 5;

        add(addHangerButton, constraints);

        constraints.gridy++;
        constraints.insets.bottom = 10;

        add(removeHangerButton, constraints);

        pack();

        initialize();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initialize() {
        for (Aircraft aircraft : aircraftService.getAll()) {
            aircraftList.addElement(aircraft.toString());
        }

        for (Hanger hanger : hangerService.getAll()) {
            hangerList.addElement(hanger.toString());
        }
    }
}
