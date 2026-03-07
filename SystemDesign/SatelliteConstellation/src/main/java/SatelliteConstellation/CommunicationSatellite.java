package SatelliteConstellation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunicationSatellite extends Satellite {
    private double bandwidth;
    private double sendingDataConsumption = 0.05;

    public CommunicationSatellite(String name, double batteryLevel, double bandwidth) {
        super(name, batteryLevel);
        this.bandwidth = bandwidth;
    }

    private void sendData(double data) {
        if (energy.getBatteryLevel() >= sendingDataConsumption) {
            System.out.println(name + ": Передача данных со скоростью " + bandwidth + " Мбит/с");
            System.out.println(name + ": Отправил " + bandwidth + " Мбит данных!");
            energy.consume(sendingDataConsumption);
        } else System.out.println("🛑 " + name + ": Недостаточно заряда для передачи данных");
    }

    @Override
    public void performMission() {
        if (state.isActive()) sendData(bandwidth);
        else System.out.println(name + ": Выключен");
    }

    @Override
    public String toString() {
        return "CommunicationSatellite{bandwidth=" + bandwidth +
                ", name='" + getName() + "'" +
                ", state=" + state +
                ", energy=EnergySystem{batteryLevel=" + energy.getBatteryLevel() + "}}";
    }
}
