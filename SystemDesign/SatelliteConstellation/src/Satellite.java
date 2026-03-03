public abstract class Satellite {

    protected String name;
    protected SatelliteState state;
    protected EnergySystem energy;


    public Satellite(String name, SatelliteState state, EnergySystem energy) {
        this.name = name;
        this.state = state;
        this.energy = energy;
    }

    public String getName() {
        return name;
    }

    protected abstract void performMission();

}