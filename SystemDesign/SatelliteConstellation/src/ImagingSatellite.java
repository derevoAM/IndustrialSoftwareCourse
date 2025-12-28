public class ImagingSatellite extends Satellite {
    private double resolution;
    private int photosTaken;
    private double takingPhotoConsumption = 0.08;

    public ImagingSatellite(String name, double batteryLevel, double resolution) {
        super(name, batteryLevel);
        this.resolution = resolution;
    }

    public double getResolution() {
        return resolution;
    }

    public int getPhotosTaken() {
        return photosTaken;
    }

    private void takePhoto() {
        photosTaken++;
        if (batteryLevel >= takingPhotoConsumption) {
            System.out.println(name + ": Съемка территории с разрешением " + resolution + "м/пиксель");
            consumeBattery(takingPhotoConsumption);

        } else System.out.println("🛑 " + name + ": Недостаточно заряда для выполнения съемки");
    }


    @Override
    public void performMission() {
        if (isActive) takePhoto();
        else {
            System.out.println("🛑 " + name + ": Выключен");
        }
    }

    @Override
    public String toString() {
        return "ImagingSatellite{resolution=" + resolution + ", photosTaken=" + photosTaken +
                ", name='" + getName() + "', isActive=" + isActive() +
                ", batteryLevel=" + getBatteryLevel() + "}";
    }


}
