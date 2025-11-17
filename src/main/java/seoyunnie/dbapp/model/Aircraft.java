package seoyunnie.dbapp.model;

import seoyunnie.dbapp.util.StatusFormatter;

public class Aircraft {
    public enum Status {
        IN_SERVICE,
        UNDER_MAINTENANCE;

        @Override
        public String toString() {
            return StatusFormatter.format(this);
        }
    }

    private final String registration;
    private final String model;
    private Status status;

    public Aircraft(String registration, String model, Status status) {
        this.registration = registration;
        this.model = model;
        this.status = status;
    }

    public Aircraft(String registration, String model, String status) {
        this(registration, model, status.equals(Status.IN_SERVICE.toString())
                ? Status.IN_SERVICE
                : Status.UNDER_MAINTENANCE);
    }

    public String getRegistration() {
        return registration;
    }

    public String getModel() {
        return model;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    @Override
    public String toString() {
        return registration + " (" + model + ")";
    }
}
