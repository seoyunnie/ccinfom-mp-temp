package seoyunnie.dbapp.model;

import seoyunnie.dbapp.util.StatusFormatter;

public class Hanger {
    public enum Status {
        AVAILABLE,
        OCCUPIED;

        @Override
        public String toString() {
            return StatusFormatter.format(this);
        }
    }

    private int id;
    private final String location;
    private Status status;

    public Hanger(String location, Status status) {
        this.location = location;
        this.status = status;
    }

    public Hanger(String location, String status) {
        this(location, status.equals(Status.AVAILABLE.toString())
                ? Status.AVAILABLE
                : Status.OCCUPIED);
    }

    public Hanger(int id, String location, Status status) {
        this(location, status);

        this.id = id;
    }

    public Hanger(int id, String location, String status) {
        this(location, status);

        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public String getLocation() {
        return location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return id + " (" + location + ")";
    }
}
