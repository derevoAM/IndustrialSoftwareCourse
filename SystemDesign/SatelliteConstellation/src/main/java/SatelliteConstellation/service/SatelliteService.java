package SatelliteConstellation.service;

import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.param.SatelliteParam;

public interface SatelliteService {
    Satellite createSatellite(SatelliteParam satelliteParam);
}
