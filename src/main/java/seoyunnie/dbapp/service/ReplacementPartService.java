package seoyunnie.dbapp.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import seoyunnie.dbapp.dao.ReplacementPartDAO;
import seoyunnie.dbapp.model.Aircraft;
import seoyunnie.dbapp.model.MaintenancePeriod;
import seoyunnie.dbapp.model.ReplacementPart;

public class ReplacementPartService {
    private final ReplacementPartDAO dao;

    public ReplacementPartService(ReplacementPartDAO dao) {
        this.dao = dao;
    }

    public List<ReplacementPart> getAll() {
        return dao.getAll();
    }

    public List<ReplacementPart> getAllByAircraft(Aircraft aircraft) {
        return dao.getAll(aircraft.getRegistration());
    }

    public List<ReplacementPart> getAllByMaintenancePeriod(MaintenancePeriod maintenancePeriod) {
        return dao.getAll(maintenancePeriod.getId());
    }

    public boolean add(ReplacementPart part) {
        if (getAll().stream().anyMatch(
                (p) -> p.getNumber() == part.getNumber() && p.getMaintenanceId() == part.getMaintenanceId())) {
            dao.update(part);

            return false;
        }

        dao.save(part);

        return true;
    }

    public Optional<File> createInventory(Aircraft aircraft) {
        var file = new File("replacement_parts_inventory.txt");

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
