package SatelliteConstellation;

public class ImagingSatelliteFactory extends SatelliteFactory{
    @Override
    public Satellite createSatellite(String name, double batteryLevel) {
        return new ImagingSatellite(name, batteryLevel, 0.0);
    }

    @Override
    public Satellite createSatelliteWithParameter(String name, double batteryLevel, double resolution) {
        return new ImagingSatellite(name, batteryLevel, resolution);
    }
}
