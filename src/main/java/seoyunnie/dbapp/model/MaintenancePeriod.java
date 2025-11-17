package seoyunnie.dbapp.model;

import java.time.LocalDate;

import seoyunnie.dbapp.util.StatusFormatter;

public class MaintenancePeriod {
    public enum Status {
        ONGOING,
        COMPLETED;

        @Override
        public String toString() {
            return StatusFormatter.format(this);
        }
    }

    private int id;
    private final String type;
    private final String aircraftRegistration;
    private final int hangerId;
    private final Status status;
    private final LocalDate startedAt;
    private LocalDate completedAt = null;

    public MaintenancePeriod(String type, String aircraftRegistration, int hangerId, Status status,
            LocalDate startedAt) {
        this.type = type;
        this.aircraftRegistration = aircraftRegistration;
        this.hangerId = hangerId;
        this.status = status;
        this.startedAt = startedAt;
    }

    public MaintenancePeriod(int id, String type, String aircraftRegistration, int hangerId, Status status,
            LocalDate startedAt) {
        this(type, aircraftRegistration, hangerId, status, startedAt);

        this.id = id;
    }

    public MaintenancePeriod(String type, String aircraftRegistration, int hangerId, String status,
            LocalDate startedAt) {
        this(
                type,
                aircraftRegistration,
                hangerId,
                status.equals(Status.ONGOING.toString())
                        ? Status.ONGOING
                        : Status.COMPLETED,
                startedAt);
    }

    public MaintenancePeriod(int id, String type, String aircraftRegistration, int hangerId, String status,
            LocalDate startedAt) {
        this(type, aircraftRegistration, hangerId, status, startedAt);

        this.id = id;
    }

    public MaintenancePeriod(String type, String aircraftRegistration, int hangerId, String status,
            LocalDate startedAt, LocalDate completedAt) {
        this(type, aircraftRegistration, hangerId, status, startedAt);

        this.completedAt = completedAt;
    }

    public MaintenancePeriod(int id, String type, String aircraftRegistration, int hangerId, String status,
            LocalDate startedAt, LocalDate completedAt) {
        this(id, type, aircraftRegistration, hangerId, status, startedAt);

        this.completedAt = completedAt;
    }

    public MaintenancePeriod(String type, String aircraftRegistration, int hangerId, Status status,
            LocalDate startedAt, LocalDate completedAt) {
        this(type, aircraftRegistration, hangerId, status, startedAt);

        this.completedAt = completedAt;
    }

    public MaintenancePeriod(int id, String type, String aircraftRegistration, int hangerId, Status status,
            LocalDate startedAt, LocalDate completedAt) {
        this(id, type, aircraftRegistration, hangerId, status, startedAt);

        this.completedAt = completedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public String getType() {
        return type;
    }

    public String getAircraftRegistration() {
        return aircraftRegistration;
    }

    public int getHangerId() {
        return hangerId;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getStartedAt() {
        return startedAt;
    }

    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDate newCompletedAt) {
        this.completedAt = newCompletedAt;
    }

    @Override
    public String toString() {
        return startedAt + " - " + (completedAt != null ? completedAt : "Ongoing");
    }
}
