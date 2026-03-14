package SatelliteConstellation.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Builder
public class EnergySystem {

    private double batteryLevel;

    @Builder.Default
    private double lowBatteryThreshold = 0.2;

    private static double MAX_BATTERY = 1.0;
    private static double MIN_BATTERY = 0.0;

    public boolean consume(double consumption) {
        if(consumption <= 0 || batteryLevel <= MIN_BATTERY) return false;

        batteryLevel = Math.max(MIN_BATTERY, batteryLevel - consumption);
        return true;
    }

    public boolean hasSufficientPower(){
        return batteryLevel >= lowBatteryThreshold;
    }

}
