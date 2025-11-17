package seoyunnie.dbapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seoyunnie.dbapp.dao.AircraftCapacityDAO;
import seoyunnie.dbapp.dao.AircraftDAO;
import seoyunnie.dbapp.model.Aircraft;
import seoyunnie.dbapp.model.AircraftCapacity;

public class AircraftService {
    private final AircraftDAO dao;
    private final AircraftCapacityDAO capacityDAO;

    private final Set<Aircraft> cache = new HashSet<>();
    private final Set<AircraftCapacity> capacityCache = new HashSet<>();

    public AircraftService(AircraftDAO dao, AircraftCapacityDAO capacityDAO) {
        this.dao = dao;
        this.capacityDAO = capacityDAO;
    }

    public Optional<Aircraft> getByRegistration(String registration) {
        Optional<Aircraft> aircraft = cache.stream().filter((a) -> a.getRegistration().equals(registration))
                .findFirst();

        if (aircraft.isEmpty()) {
            aircraft = dao.get(registration);

            if (aircraft.isPresent()) {
                cache.add(aircraft.get());
            }
        }

        return aircraft;
    }

    public Optional<AircraftCapacity> getCapacityByModel(String model) {
        Optional<AircraftCapacity> aircraftCapacity = capacityCache.stream().filter((c) -> c.getModel().equals(model))
                .findFirst();

        if (aircraftCapacity.isEmpty()) {
            aircraftCapacity = capacityDAO.get(model);

            if (aircraftCapacity.isPresent()) {
                capacityCache.add(aircraftCapacity.get());
            }
        }

        return aircraftCapacity;
    }

    public List<Aircraft> getAll() {
        List<Aircraft> fleet = dao.getAll();

        for (Aircraft aircraft : fleet) {
            if (!cache.stream().anyMatch((a) -> a.getRegistration().equals(aircraft.getRegistration()))) {
                cache.add(aircraft);
            }
        }

        return fleet;
    }

    public boolean add(Aircraft aircraft) {
        if (cache.stream().anyMatch((a) -> a.getRegistration().equals(aircraft.getRegistration())) ||
                aircraft.getRegistration().length() > 10 ||
                aircraft.getModel().length() > 10) {
            return false;
        }

        dao.save(aircraft);

        return true;
    }

    public void addCapacity(AircraftCapacity aircraftCapacity) {
        capacityDAO.save(aircraftCapacity);
    }

    public void removeByRegistration(String registration) {
        dao.delete(registration);
    }
}
