package SatelliteConstellation.param;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class SatelliteParam {
    private SatelliteType type;
    private String name;
    private double batteryLevel;
}
