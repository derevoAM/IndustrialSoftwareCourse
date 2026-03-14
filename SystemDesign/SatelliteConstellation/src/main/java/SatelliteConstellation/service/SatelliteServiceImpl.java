package SatelliteConstellation.service;

import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.exception.AuthExceptions;
import SatelliteConstellation.factory.SatelliteFactory;
import SatelliteConstellation.param.SatelliteParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SatelliteServiceImpl implements SatelliteService {
    private final List<SatelliteFactory> factories;

    public SatelliteServiceImpl(List<SatelliteFactory> factories) {
        this.factories = factories;
    }

    @Override
    public Satellite createSatellite(SatelliteParam satelliteParam) {
        SatelliteFactory factory = factories.stream()
                .filter(f -> f.isSatelliteTypeSupported(satelliteParam.getType()))
                .findFirst()
                .orElseThrow(() -> new AuthExceptions.NoExistingFactoryException());
        return factory.createSatelliteWithParameter(satelliteParam);
    }
}
