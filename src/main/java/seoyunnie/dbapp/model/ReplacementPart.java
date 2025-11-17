package seoyunnie.dbapp.model;

public class ReplacementPart {
    private final int number;
    private final String name;
    private final int amount;
    private final String aircraftRegistration;
    private final int maintenanceId;

    public ReplacementPart(int number, String name, int amount, String aircraftRegistration, int maintenanceId) {
        this.number = number;
        this.name = name;
        this.amount = amount;
        this.aircraftRegistration = aircraftRegistration;
        this.maintenanceId = maintenanceId;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getAircraftRegistration() {
        return aircraftRegistration;
    }

    public int getMaintenanceId() {
        return maintenanceId;
    }

    @Override
    public String toString() {
        return amount + "x " + name + " (#" + number + ")";
    }
}
