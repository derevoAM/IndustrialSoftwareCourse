package SatelliteConstellation.factory;

import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.param.SatelliteParam;
import SatelliteConstellation.param.SatelliteType;

public interface SatelliteFactory {
    Satellite createSatelliteWithParameter(SatelliteParam satelliteParam);
    boolean isSatelliteTypeSupported(SatelliteType type);
}
