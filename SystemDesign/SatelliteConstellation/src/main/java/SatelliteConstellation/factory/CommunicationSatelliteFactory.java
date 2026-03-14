package SatelliteConstellation.factory;

import SatelliteConstellation.domain.CommunicationSatellite;
import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.exception.AuthExceptions;
import SatelliteConstellation.param.CommunicationSatelliteParam;
import SatelliteConstellation.param.SatelliteParam;
import SatelliteConstellation.param.SatelliteType;
import org.springframework.stereotype.Component;

@Component
public class CommunicationSatelliteFactory implements SatelliteFactory {

    @Override
    public Satellite createSatelliteWithParameter(SatelliteParam satelliteParam) {
        if (!(satelliteParam instanceof CommunicationSatelliteParam)) {
            throw new AuthExceptions.WrongSatelliteParamException();
        }
        CommunicationSatelliteParam p = (CommunicationSatelliteParam) satelliteParam;
        return new CommunicationSatellite(p.getName(), p.getBatteryLevel(), p.getBandwidth());
    }

    @Override
    public boolean isSatelliteTypeSupported(SatelliteType type) {
        return type == SatelliteType.COMMUNICATION;
    }
}
