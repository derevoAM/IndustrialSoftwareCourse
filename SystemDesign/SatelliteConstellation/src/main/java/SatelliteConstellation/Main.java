package SatelliteConstellation;

import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.param.CommunicationSatelliteParam;
import SatelliteConstellation.param.ImagingSatelliteParam;
import SatelliteConstellation.repository.ConstellationRepository;
import SatelliteConstellation.service.SatelliteService;
import SatelliteConstellation.service.SpaceOperationCenterService;
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

        SatelliteService satelliteService = context.getBean(SatelliteService.class);

        Satellite comSat1 = satelliteService.createSatellite(new CommunicationSatelliteParam("Связь-1", 0.85, 500.0));
        Satellite comSat2 = satelliteService.createSatellite(new CommunicationSatelliteParam("Связь-2", 0.75, 1000.0));

        Satellite imagingSat1 = satelliteService.createSatellite(new ImagingSatelliteParam("ДЗЗ-1", 0.92, 2.5));
        Satellite imagingSat2 = satelliteService.createSatellite(new ImagingSatelliteParam("ДЗЗ-2", 0.22, 1.0));
        Satellite imagingSat3 = satelliteService.createSatellite(new ImagingSatelliteParam("ДЗЗ-3", 0.15, 0.5));

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
