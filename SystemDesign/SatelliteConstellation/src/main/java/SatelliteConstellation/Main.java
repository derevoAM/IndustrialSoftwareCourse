package SatelliteConstellation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        ConstellationRepository constellationRepository = context.getBean(ConstellationRepository.class);
        SpaceOperationCenterService service = context.getBean(SpaceOperationCenterService.class);

        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        CommunicationSatellite comSat1 = new CommunicationSatellite("Связь-1", 0.85, 500.0);
        CommunicationSatellite comSat2 = new CommunicationSatellite("Связь-2", 0.75, 1000.0);

        ImagingSatellite imagingSat1 = new ImagingSatellite("ДЗЗ-1", 0.92, 2.5);
        ImagingSatellite imagingSat2 = new ImagingSatellite("ДЗЗ-2", 0.22, 1.0);
        ImagingSatellite imagingSat3 = new ImagingSatellite("ДЗЗ-3", 0.15, 0.5);

        System.out.println("---------------------------------------------");

        service.createAndSaveConstellation("Орбита-1");
        service.createAndSaveConstellation("Орбита-2");

        System.out.println("---------------------------------------------");

        System.out.println("\n📡 ДОБАВЛЕНИЕ СПУТНИКОВ:");

        service.addSatelliteToConstellation("Орбита-1", comSat1);
        service.addSatelliteToConstellation("Орбита-1", imagingSat1);
        service.addSatelliteToConstellation("Орбита-1", imagingSat2);
        service.addSatelliteToConstellation("Орбита-2", comSat2);
        service.addSatelliteToConstellation("Орбита-2", imagingSat3);

        System.out.println("-----------------------------------");

        service.activateAllSatellites("Орбита-1");

        service.executeConstellationMission("Орбита-1");

        service.showConstellationStatus("Орбита-1");

        System.out.println(constellationRepository.getConstellations());
    }
}
