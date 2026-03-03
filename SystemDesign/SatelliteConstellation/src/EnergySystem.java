public class EnergySystem {

    private double batteryLevel;

    public EnergySystem(double batteryLevel){
        this.batteryLevel = batteryLevel;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }
    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void consume(double consumption) {
        if(consumption > 0)
        {
            batteryLevel = Math.max(0.0, batteryLevel - consumption);
        }
    }
}
