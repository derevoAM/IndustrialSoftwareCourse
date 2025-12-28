public class CommunicationSatellite extends Satellite {
    private double bandwidth;
    private double sendingDataConsumption = 0.05;

    public CommunicationSatellite(String name, double batteryLevel, double bandwidth) {
        super(name, batteryLevel);
        this.bandwidth = bandwidth;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    private void sendData(double data) {
        if (batteryLevel >= sendingDataConsumption) {
            System.out.println(name + ": Передача данных со скоростью " + bandwidth + " Мбит/с");
            System.out.println(name + ": Отправил " + data + " Мбит данных");
            consumeBattery(sendingDataConsumption);

        } else System.out.println("🛑 " + name + ": Недостаточно заряда для передачи данных");
    }


    @Override
    public void performMission() {
        if (isActive) sendData(1000);
        else System.out.println(name + ": Выключен");
    }

    @Override
    public String toString() {
        return "CommunicationSatellite{bandwidth=" + bandwidth +
                ", name='" + getName() + "', isActive=" + isActive() +
                ", batteryLevel=" + getBatteryLevel() + "}";
    }


}