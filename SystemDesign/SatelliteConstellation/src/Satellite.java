public abstract class Satellite {

    protected String name;
    protected boolean isActive;
    protected double batteryLevel;


    public Satellite(String name, double batteryLevel) {
        this.name = name;
        this.batteryLevel = batteryLevel;
        isActive = false;
        System.out.println("Создан спутник: " + name + " (заряд: " + (int) (batteryLevel * 100) + "%)");

    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public boolean activate() {
        isActive = batteryLevel >= 0.2;
        if (isActive) System.out.println(name + ": Активация успешна");
        else System.out.println(name + ": Ошибка активации (заряд: " + batteryLevel + ")");
        return isActive;
    }


    public void deactivate() {
        if (isActive) {
            isActive = false;
            System.out.println(name + ": Деактивирован");
        }
    }


    public void consumeBattery(double consumption) {
        batteryLevel -= consumption;
        if (batteryLevel < 0.2) deactivate();

    }


    protected abstract void performMission();

}