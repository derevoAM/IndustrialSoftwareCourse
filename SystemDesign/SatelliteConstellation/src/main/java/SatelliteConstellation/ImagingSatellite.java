package SatelliteConstellation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagingSatellite extends Satellite {
    private double resolution;
    private int photosTaken;
    private double takingPhotoConsumption = 0.08;

    public ImagingSatellite(String name, double batteryLevel, double resolution) {
        super(name, batteryLevel);
        this.resolution = resolution;
        this.photosTaken = 0;
    }


    private void takePhoto() {
        if (energy.getBatteryLevel() >= takingPhotoConsumption) {
            System.out.println(name + ": Съемка территории с разрешением " + resolution + " м/пиксель");
            photosTaken++;
            System.out.println(name + ": Снимок #" + photosTaken + " сделан!");
            energy.consume(takingPhotoConsumption);
        } else System.out.println("🛑 " + name + ": Недостаточно заряда для выполнения съемки");
    }

    @Override
    public void performMission() {
        if (state.isActive()) takePhoto();
        else {
            System.out.println("🛑 " + name + ": Выключен");
        }
    }

    @Override
    public String toString() {
        return "ImagingSatellite{photosTaken=" + photosTaken +
                ", name='" + getName() + "'" +
                ", state=" + state +
                ", energy=EnergySystem{batteryLevel=" + energy.getBatteryLevel() + "}}";
    }
}
