public class SatelliteState {
    private boolean isActive;

    public SatelliteState()
    {
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }


    public boolean activate(boolean hasSufficientPower) {
        if (hasSufficientPower && !isActive) {
            isActive = true;
            return true;
        }
        return false;
    }


    public boolean deactivate() {
        if (isActive) {
            isActive = false;
            return true;
        }
        return false;
    }


}
