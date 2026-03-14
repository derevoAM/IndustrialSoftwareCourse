package SatelliteConstellation.service.request;

import SatelliteConstellation.param.SatelliteParam;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddSatelliteRequest {
    private String constellationName;
    private SatelliteParam satelliteParam;
}
