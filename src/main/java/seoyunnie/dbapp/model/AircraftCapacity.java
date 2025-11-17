package seoyunnie.dbapp.model;

public class AircraftCapacity {
    private final String model;
    private final int capacity;

    public AircraftCapacity(String model, int capacity) {
        this.model = model;
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public int getCapacity() {
        return capacity;
    }
}
