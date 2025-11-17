package seoyunnie.dbapp.model;

import java.sql.Date;
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

    private final int id;
    private final String type;
    private final String aircraftRegistration;
    private final int hangerId;
    private final Status status;
    private final LocalDate startedAt;
    private LocalDate completedAt = null;

    public MaintenancePeriod(int id, String type, String aircraftRegistration, int hangerId, Status status,
            Date startedAt) {
        this.id = id;
        this.type = type;
        this.aircraftRegistration = aircraftRegistration;
        this.hangerId = hangerId;
        this.status = status;
        this.startedAt = startedAt.toLocalDate();
    }

    public MaintenancePeriod(int id, String type, String aircraftRegistration, int hangerId, String status,
            Date startedAt) {
        this(
                id,
                type,
                aircraftRegistration,
                hangerId,
                status.equals(Status.ONGOING.toString())
                        ? Status.ONGOING
                        : Status.COMPLETED,
                startedAt);
    }

    public MaintenancePeriod(int id, String type, String aircraftRegistration, int hangerId, String status,
            Date startedAt, Date completedAt) {
        this(id, type, aircraftRegistration, hangerId, status, startedAt);

        this.completedAt = completedAt.toLocalDate();
    }

    public MaintenancePeriod(int id, String type, String aircraftRegistration, int hangerId, Status status,
            Date startedAt, Date completedAt) {
        this(id, type, aircraftRegistration, hangerId, status, startedAt);

        this.completedAt = completedAt.toLocalDate();
    }

    public int getId() {
        return id;
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
}
