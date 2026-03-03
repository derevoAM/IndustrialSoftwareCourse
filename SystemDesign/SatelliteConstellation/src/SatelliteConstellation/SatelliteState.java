package SatelliteConstellation;

public class SatelliteState {
    private boolean isActive;
    private String statusMessage;

    public SatelliteState() {
        this.isActive = false;
        this.statusMessage = "Не активирован";
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean activate(boolean hasSufficientPower) {
        if (hasSufficientPower && !isActive) {
            isActive = true;
            statusMessage = "Активен";
            return true;
        }
        return false;
    }

    public boolean deactivate() {
        if (isActive) {
            isActive = false;
            statusMessage = "Деактивирован";
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "SatelliteState{isActive=" + isActive + ", statusMessage='" + statusMessage + "'}";
    }
}
