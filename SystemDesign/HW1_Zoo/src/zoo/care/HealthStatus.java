package zoo.care;

public enum HealthStatus {
    HEALTHY("Здоров"),
    SICK("Болен");

    private final String status;

    HealthStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}