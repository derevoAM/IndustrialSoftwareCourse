package SatelliteConstellation;

abstract public class SatelliteFactory {
    public abstract Satellite createSatellite(String name, double batteryLevel);
    public abstract Satellite createSatelliteWithParameter(String name, double batteryLevel, double parameter);
}
