package SatelliteConstellation;

import SatelliteConstellation.param.CommunicationSatelliteParam;
import SatelliteConstellation.param.ImagingSatelliteParam;
import SatelliteConstellation.repository.ConstellationRepository;
import SatelliteConstellation.service.SpaceOperationCenterService;
import SatelliteConstellation.service.request.AddSatelliteRequest;
import SatelliteConstellation.service.request.MissionRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        SpaceOperationCenterService facade = context.getBean(SpaceOperationCenterService.class);
        ConstellationRepository constellationRepository = context.getBean(ConstellationRepository.class);

        System.out.println("ДОБАВЛЕНИЕ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        facade.addSatellite(new AddSatelliteRequest("Орбита-1", new CommunicationSatelliteParam("Связь-1", 0.85, 500.0)));
        facade.addSatellite(new AddSatelliteRequest("Орбита-1", new ImagingSatelliteParam("ДЗЗ-1", 0.92, 2.5)));
        facade.addSatellite(new AddSatelliteRequest("Орбита-1", new ImagingSatelliteParam("ДЗЗ-2", 0.22, 1.0)));

        facade.addSatellite(new AddSatelliteRequest("Орбита-2", new CommunicationSatelliteParam("Связь-2", 0.75, 1000.0)));
        facade.addSatellite(new AddSatelliteRequest("Орбита-2", new ImagingSatelliteParam("ДЗЗ-3", 0.15, 0.5)));

        System.out.println("---------------------------------------------");

        facade.executeMission(new MissionRequest("Орбита-1"));

        System.out.println(constellationRepository.getConstellations());
    }
}
