package seoyunnie.dbapp;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.formdev.flatlaf.FlatLightLaf;

import seoyunnie.dbapp.dao.AircraftCapacityDAO;
import seoyunnie.dbapp.dao.AircraftDAO;
import seoyunnie.dbapp.dao.HangerDAO;
import seoyunnie.dbapp.dao.MaintenancePeriodDAO;
import seoyunnie.dbapp.dao.ReplacementPartDAO;
import seoyunnie.dbapp.gui.DashboardFrame;
import seoyunnie.dbapp.service.AircraftService;
import seoyunnie.dbapp.service.HangerService;
import seoyunnie.dbapp.service.MaintenanceService;
import seoyunnie.dbapp.service.ReplacementPartService;

public class DBApp {
    private static Connection connection = null;

    public static void main(String[] args) {
        FlatLightLaf.setup();

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/aircraft_maintenance",
                    args[0], args[1]);
        } catch (SQLException e) {
            e.printStackTrace();

            System.exit(1);
        }

        var aircraftDAO = new AircraftDAO(connection);
        var aircraftCapacityDAO = new AircraftCapacityDAO(connection);
        var hangerDAO = new HangerDAO(connection);
        var replacementPartDAO = new ReplacementPartDAO(connection);
        var maintenancePeriodDAO = new MaintenancePeriodDAO(connection);

        var aircraftService = new AircraftService(aircraftDAO, aircraftCapacityDAO);
        var hangerService = new HangerService(hangerDAO);
        var replacementPartService = new ReplacementPartService(replacementPartDAO);
        var maintenanceService = new MaintenanceService(
                maintenancePeriodDAO,
                aircraftService, hangerService, replacementPartService);

        var dashboardFrame = new DashboardFrame(
                aircraftService, hangerService, maintenanceService, replacementPartService);

        dashboardFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                int exitCode = 0;

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();

                    exitCode = 1;
                }

                dashboardFrame.dispose();

                System.exit(exitCode);
            }
        });
    }
}
