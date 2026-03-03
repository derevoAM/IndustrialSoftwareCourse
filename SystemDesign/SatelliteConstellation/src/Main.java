public class Main {
    public static void main(String[] args) {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================\n");

        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");


        SatelliteState comState1 = new SatelliteState();
        SatelliteState comState2 = new SatelliteState();
        SatelliteState imagState1 = new SatelliteState();
        SatelliteState imagState2 = new SatelliteState();
        SatelliteState imagState3 = new SatelliteState();

        EnergySystem comEnergy1 = new EnergySystem(0.85);
        EnergySystem comEnergy2 = new EnergySystem(0.75);
        EnergySystem imagEnergy1 = new EnergySystem(0.92);
        EnergySystem imagEnergy2 = new EnergySystem(0.22);
        EnergySystem imagEnergy3 = new EnergySystem(0.15);


        CommunicationSatellite comSat1 = new CommunicationSatellite("Связь-1", comState1, comEnergy1, 500.0);
        CommunicationSatellite comSat2 = new CommunicationSatellite("Связь-2", comState2, comEnergy2, 1000.0);

        ImagingSatellite imagingSat1 = new ImagingSatellite("ДЗЗ-1", imagState1, imagEnergy1, 2.5);
        ImagingSatellite imagingSat2 = new ImagingSatellite("ДЗЗ-2", imagState2, imagEnergy2, 1.0);
        ImagingSatellite imagingSat3 = new ImagingSatellite("ДЗЗ-3", imagState3, imagEnergy3, 0.5);

        System.out.println("---------------------------------------------\n");


        SatelliteConstellation constellation = new SatelliteConstellation("RU Basic");
        System.out.println("---------------------------------------------");

        System.out.println("\nФОРМИРОВАНИЕ ГРУППИРОВКИ:");
        System.out.println("-----------------------------------");

        constellation.addSatellite(comSat1);
        constellation.addSatellite(comSat2);
        constellation.addSatellite(imagingSat1);
        constellation.addSatellite(imagingSat2);
        constellation.addSatellite(imagingSat3);

        System.out.println("-----------------------------------");

        // Выводим список спутников
        System.out.println(constellation.getSatellites());
        System.out.println("-----------------------------------\n");

        System.out.println("АКТИВАЦИЯ СПУТНИКОВ:");
        System.out.println("-------------------------");

        // Активируем спутники
        for (Satellite satellite : constellation.getSatellites()) {
            boolean activated = satellite.state.activate(satellite.energy.getBatteryLevel());
        }

        System.out.println("\nВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ RU BASIC");
        System.out.println("==================================================");

        constellation.executeAllMissions();

        System.out.println("\n" + constellation.getSatellites());
    }
}