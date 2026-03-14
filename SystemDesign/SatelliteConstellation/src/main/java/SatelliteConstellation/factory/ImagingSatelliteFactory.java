package SatelliteConstellation.factory;

import SatelliteConstellation.domain.ImagingSatellite;
import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.exception.AuthExceptions;
import SatelliteConstellation.param.ImagingSatelliteParam;
import SatelliteConstellation.param.SatelliteParam;
import SatelliteConstellation.param.SatelliteType;
import org.springframework.stereotype.Component;

@Component
public class ImagingSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam satelliteParam) {
        if (!(satelliteParam instanceof ImagingSatelliteParam)) {
            throw new AuthExceptions.WrongSatelliteParamException();
        }
        ImagingSatelliteParam p = (ImagingSatelliteParam) satelliteParam;
        return new ImagingSatellite(p.getName(), p.getBatteryLevel(), p.getResolution());
    }

    @Override
    public boolean isSatelliteTypeSupported(SatelliteType type) {
        return type == SatelliteType.IMAGE;
    }
}
