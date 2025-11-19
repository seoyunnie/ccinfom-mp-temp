package seoyunnie.dbapp.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seoyunnie.dbapp.dao.MaintenancePeriodDAO;
import seoyunnie.dbapp.model.Aircraft;
import seoyunnie.dbapp.model.Hanger;
import seoyunnie.dbapp.model.MaintenancePeriod;

public class MaintenanceService {
    public static final int SUCCESS = 0;
    public static final int ALREADY_EXISTS = 1;
    public static final int TOO_LONG_STRING = 3;

    private final MaintenancePeriodDAO dao;

    private final AircraftService aircraftService;
    private final HangerService hangerService;
    private final ReplacementPartService replacementPartService;

    private final Set<MaintenancePeriod> cache = new HashSet<>();

    public MaintenanceService(MaintenancePeriodDAO dao, AircraftService aircraftService, HangerService hangerService,
            ReplacementPartService replacementPartService) {
        this.dao = dao;

        this.aircraftService = aircraftService;
        this.hangerService = hangerService;
        this.replacementPartService = replacementPartService;
    }

    public List<MaintenancePeriod> getAllByAircraft(Aircraft aircraft) {
        List<MaintenancePeriod> periods = dao.getAll(aircraft.getRegistration());

        for (MaintenancePeriod period : periods) {
            if (!cache.stream().anyMatch((p) -> p.getId() == period.getId())) {
                cache.add(period);
            }
        }

        return periods;
    }

    public List<MaintenancePeriod> getAllByHanger(Hanger hanger) {
        List<MaintenancePeriod> periods = dao.getAll(hanger.getId());

        for (MaintenancePeriod period : periods) {
            if (!cache.stream().anyMatch((p) -> p.getId() == period.getId())) {
                cache.add(period);
            }
        }

        return periods;
    }

    public int scheduleMaintenance(MaintenancePeriod period) {
        if (cache.stream().anyMatch((p) -> p.getAircraftRegistration().equals(period.getAircraftRegistration()) &&
                p.getCompletedAt() == null)) {
            return ALREADY_EXISTS;
        } else if (period.getType().length() > 25) {
            return TOO_LONG_STRING;
        }

        dao.save(period);

        Aircraft aircraft = aircraftService.getByRegistration(period.getAircraftRegistration()).get();
        aircraft.setStatus(Aircraft.Status.UNDER_MAINTENANCE);

        aircraftService.update(aircraft);

        Hanger hanger = hangerService.getById(period.getHangerId()).get();
        hanger.setStatus(Hanger.Status.OCCUPIED);

        hangerService.update(hanger);

        return SUCCESS;
    }

    public void completeMaintenance(MaintenancePeriod period) {
        if (period.getCompletedAt() != null) {
            return;
        }

        period.setCompletedAt(LocalDate.now());

        dao.update(period);

        Aircraft aircraft = aircraftService.getByRegistration(period.getAircraftRegistration()).get();
        aircraft.setStatus(Aircraft.Status.IN_SERVICE);

        aircraftService.update(aircraft);

        Hanger hanger = hangerService.getById(period.getHangerId()).get();
        hanger.setStatus(Hanger.Status.AVAILABLE);

        hangerService.update(hanger);
    }

    public Optional<File> createReport(MaintenancePeriod period) {
        var file = new File(period.getAircraftRegistration() + "_maintenance_report.txt");

        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            fw.write("ID: " + period.getId() + "\n");
            fw.write("Maintenance Type: " + period.getType() + "\n");
            fw.write("Status: " + period.getStatus() + "\n");
            fw.write("Started at: " + period.getStartedAt() + "\n");

            var completedAt = period.getCompletedAt();

            fw.write("Completed at: " + (completedAt != null ? completedAt.toString() : "Ongoing") + "\n");

            fw.write("\n");

            fw.write(replacementPartService.getAllByMaintenancePeriod(period).stream().map((p) -> p.toString())
                    .collect(Collectors.joining("\n")));

            return Optional.of(file);
        } catch (IOException e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }

    public Optional<File> createScheduleReport(Aircraft aircraft) {
        var file = new File(aircraft.getRegistration() + "_maintenance_schedule.txt");

        try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
            fw.write("Registration Mark: " + aircraft.getRegistration() + "\n");
            fw.write("Model: " + aircraft.getModel() + "\n");

            fw.write("\n");

            fw.write(getAllByAircraft(aircraft).stream().map((p) -> p.toString()).collect(Collectors.joining("\n")));

            return Optional.of(file);
        } catch (IOException e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }
}
