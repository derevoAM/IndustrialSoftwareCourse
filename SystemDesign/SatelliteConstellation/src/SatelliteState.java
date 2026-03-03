public class SatelliteState {
    private boolean isActive;

    public SatelliteState()
    {
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }


    public boolean activate(double batteryLevel) {
        isActive = batteryLevel >= 0.2;
        if (isActive) System.out.println("Активация успешна");
        else System.out.println("Ошибка активации (заряд: " + batteryLevel + ")");
        return isActive;
    }


    public void deactivate() {
        if (isActive) {
            isActive = false;
            System.out.println("Деактивирован");
        }
    }

    public void updateState(double batteryLevel)
    {
        if(batteryLevel <= 0.2 && isActive){
            System.out.println("Низкий заряд. Деактивация");
            deactivate();
        }
    }

}
