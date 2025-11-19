package seoyunnie.dbapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seoyunnie.dbapp.dao.HangerDAO;
import seoyunnie.dbapp.model.Hanger;

public class HangerService {
    public static final int SUCCESS = 0;
    public static final int ALREADY_EXISTS = 1;
    public static final int TOO_LONG_STRING = 3;

    private final HangerDAO dao;

    private final Set<Hanger> cache = new HashSet<>();

    public HangerService(HangerDAO dao) {
        this.dao = dao;
    }

    public Optional<Hanger> getById(int id) {
        Optional<Hanger> hanger = cache.stream().filter((h) -> h.getId() == id).findFirst();

        if (hanger.isEmpty()) {
            hanger = dao.get(id);

            hanger.ifPresent((h) -> cache.add(h));
        }

        return hanger;
    }

    public List<Hanger> getAll() {
        List<Hanger> hangers = dao.getAll();

        cache.clear();
        cache.addAll(hangers);

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

    public int add(Hanger hanger) {
        if (cache.stream().anyMatch((h) -> h.getLocation().equals(hanger.getLocation()))) {
            return ALREADY_EXISTS;
        } else if (hanger.getLocation().length() > 255) {
            return TOO_LONG_STRING;
        }

        dao.save(hanger);

        return SUCCESS;
    }

    public void update(Hanger hanger) {
        dao.update(hanger);
    }

    public void removeById(int id) {
        dao.delete(id);
    }
}
