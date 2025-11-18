package seoyunnie.dbapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seoyunnie.dbapp.dao.HangerDAO;
import seoyunnie.dbapp.model.Hanger;

public class HangerService {
    private final HangerDAO dao;

    private final Set<Hanger> cache = new HashSet<>();

    public HangerService(HangerDAO dao) {
        this.dao = dao;
    }

    public Optional<Hanger> getById(int id) {
        Optional<Hanger> hanger = cache.stream().filter((h) -> h.getId() == id).findFirst();

        if (hanger.isEmpty()) {
            hanger = dao.get(id);

            if (hanger.isPresent()) {
                cache.add(hanger.get());
            }
        }

        return hanger;
    }

    public List<Hanger> getAll() {
        List<Hanger> hangers = dao.getAll();

        for (Hanger hanger : hangers) {
            if (!cache.stream().anyMatch((a) -> a.getId() == hanger.getId())) {
                cache.add(hanger);
            }
        }

        return hangers;
    }

    public List<Hanger> getAllAvailable() {
        List<Hanger> availableHangers = dao.getAll();

        for (Hanger hanger : dao.getAll()) {
            if (!cache.stream().anyMatch((a) -> a.getId() == hanger.getId())) {
                cache.add(hanger);
            }

            if (hanger.getStatus().equals(Hanger.Status.AVAILABLE)) {
                availableHangers.add(hanger);
            }
        }

        return availableHangers;
    }

    public boolean add(Hanger hanger) {
        if (cache.stream().anyMatch((h) -> h.getLocation().equals(hanger.getLocation())) ||
                hanger.getLocation().length() > 255) {
            return false;
        }

        dao.save(hanger);

        return true;
    }

    public boolean update(Hanger hanger) {
        if (!cache.stream().anyMatch((h) -> h.getLocation().equals(hanger.getLocation())) ||
                hanger.getLocation().length() > 255) {
            return false;
        }

        dao.update(hanger);

        return true;
    }

    public void removeById(int id) {
        dao.delete(id);
    }
}
