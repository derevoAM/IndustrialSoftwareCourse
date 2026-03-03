public class EnergySystem {

    private double batteryLevel;
    private static double LOW_BATTERY_THRESHOLD = 0.2;
    private static double MAX_BATTERY = 1.0;
    private static double MIN_BATTERY = 0.0;

    public EnergySystem(double batteryLevel){
        this.batteryLevel = Math.max(MIN_BATTERY, Math.min(batteryLevel, MAX_BATTERY));
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public boolean consume(double consumption) {
        if(consumption <= 0 || batteryLevel <= MIN_BATTERY) return false;

        batteryLevel = Math.max(MIN_BATTERY, batteryLevel - consumption);
        return true;
    }

    public boolean hasSufficientPower(){
        return batteryLevel >= LOW_BATTERY_THRESHOLD;
    }

}
