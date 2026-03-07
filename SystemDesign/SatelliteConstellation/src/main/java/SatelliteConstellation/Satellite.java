package SatelliteConstellation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Satellite {

    protected String name;
    protected SatelliteState state;
    protected EnergySystem energy;

    public Satellite(String name, double batteryLevel) {
        this.name = name;
        this.state = new SatelliteState();
        this.energy = EnergySystem.builder().batteryLevel(batteryLevel).build();
        System.out.println("Создан спутник: " + name + " (" + batteryLevel + ")");
    }


    public boolean activate() {
        if (state.activate(energy.hasSufficientPower())) {
            System.out.println("✅ " + name + ": Активация успешна");
            return true;
        }
        System.out.println("❌ " + name + ": Ошибка активации");
        return false;
    }

    public void deactivate() {
        if (state.deactivate()) {
            System.out.println(name + ": Деактивирован");
        } else System.out.println(name + ": Ошибка деактивации - уже деактивирован");
    }

    protected abstract void performMission();

    public void updateState()
    {
        if(!energy.hasSufficientPower())
        {
            deactivate();
        }
    }

}
