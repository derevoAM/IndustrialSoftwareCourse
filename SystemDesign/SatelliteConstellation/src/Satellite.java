public abstract class Satellite {

    protected String name;
    protected SatelliteState state;
    protected EnergySystem energy;


    public Satellite(String name, double batteryLevel) {
        this.name = name;
        this.state = new SatelliteState();
        this.energy = new EnergySystem(batteryLevel);
        System.out.println(name + ": Создан");

    }

    public SatelliteState getState() {
        return state;
    }
    public EnergySystem getEnergy() {
        return energy;
    }

    public boolean activate(){
        if(state.activate(energy.hasSufficientPower())){
            System.out.println(name + ": Активирован");
            return true;
        }
        System.out.println(name + ": Ошибка активации");
        return false;
    }

    public void deactivate()
    {
        if(state.deactivate()){
            System.out.println(name + ": Деактивирован");
        }
        else System.out.println(name + ": Ошибка деактивации - уже деактивирован");
    }

    public String getName() {
        return name;
    }

    protected abstract void performMission();

}