package SatelliteConstellation;

public class CommunicationSatelliteFactory extends SatelliteFactory{
    @Override
    public Satellite createSatellite(String name, double batteryLevel) {
        return new CommunicationSatellite(name, batteryLevel, 0.0);
    }

    @Override
    public Satellite createSatelliteWithParameter(String name, double batteryLevel, double bandwidth) {
        return new CommunicationSatellite(name, batteryLevel, bandwidth);
    }
}
