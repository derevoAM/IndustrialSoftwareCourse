package SatelliteConstellation.service;

import SatelliteConstellation.aspect.Timed;
import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.service.request.AddSatelliteRequest;
import SatelliteConstellation.service.request.MissionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceOperationCenterService {

    private final ConstellationService constellationService;
    private final SatelliteService satelliteService;

    @Timed("addSatellite")
    public void addSatellite(AddSatelliteRequest satelliteRequest) {
        if (!constellationService.isConstellationInRepository(satelliteRequest.getConstellationName()))
            constellationService.createAndSaveConstellation(satelliteRequest.getConstellationName());

        Satellite satellite = satelliteService.createSatellite(satelliteRequest.getSatelliteParam());
        satellite.activate();
        constellationService.addSatelliteToConstellation(satelliteRequest.getConstellationName(), satellite);
    }

    @Timed("executeMission")
    public void executeMission(MissionRequest missionRequest) {
        if (constellationService.isConstellationInRepository(missionRequest.getConstellationName()))
            constellationService.executeConstellationMission(missionRequest.getConstellationName());
        else throw new RuntimeException("Группировки с таким названием не существует");
    }


}
