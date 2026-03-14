package SatelliteConstellation;

import SatelliteConstellation.domain.CommunicationSatellite;
import SatelliteConstellation.domain.ImagingSatellite;
import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.domain.SatelliteConstellation;
import SatelliteConstellation.factory.CommunicationSatelliteFactory;
import SatelliteConstellation.factory.ImagingSatelliteFactory;
import SatelliteConstellation.factory.SatelliteFactory;
import SatelliteConstellation.param.CommunicationSatelliteParam;
import SatelliteConstellation.param.ImagingSatelliteParam;
import SatelliteConstellation.repository.ConstellationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConstellationRepositoryIntegrationTest {

    private static final String CONSTELLATION_NAME = "Тест-Орбита";

    @Autowired
    private ConstellationRepository constellationRepository;

    SatelliteFactory imagingFactory = new ImagingSatelliteFactory();
    SatelliteFactory communicationFactory = new CommunicationSatelliteFactory();

    @BeforeEach
    void cleanUp() {
        constellationRepository.delete(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("Создание группировки сохраняет её в репозиторий")
    void givenConstellation_whenCreating_thenStoredInRepository() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);

        constellationRepository.create(CONSTELLATION_NAME, constellation);

        assertNotNull(constellationRepository.get(CONSTELLATION_NAME));
    }

    @Test
    @DisplayName("Добавление спутника увеличивает размер группировки")
    void givenConstellation_whenAddingSatellite_thenSizeIncreases() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);
        constellationRepository.create(CONSTELLATION_NAME, constellation);

        constellation.addSatellite(imagingFactory.createSatelliteWithParameter(new ImagingSatelliteParam("ДЗЗ-Тест", 0.9, 2.5)));

        assertEquals(1, constellationRepository.get(CONSTELLATION_NAME).getSatellites().size());
    }

    @Test
    @DisplayName("Активация спутника меняет его статус на активен")
    void givenSatellite_whenActivating_thenIsActiveTrue() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);
        Satellite satellite = imagingFactory.createSatelliteWithParameter(new ImagingSatelliteParam("ДЗЗ-Тест", 0.9, 2.5));
        constellation.addSatellite(satellite);
        constellationRepository.create(CONSTELLATION_NAME, constellation);

        assertFalse(satellite.getState().isActive());
        satellite.activate();

        assertTrue(satellite.getState().isActive());
    }

    @Test
    @DisplayName("Выполнение миссии ImagingSatellite увеличивает счётчик снимков")
    void givenActivatedImagingSatellite_whenExecutingMission_thenPhotosTakenIncreases() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);
        ImagingSatellite satellite = (ImagingSatellite) imagingFactory.createSatelliteWithParameter(new ImagingSatelliteParam("ДЗЗ-Тест", 0.9, 2.5));
        constellation.addSatellite(satellite);
        constellationRepository.create(CONSTELLATION_NAME, constellation);
        satellite.activate();

        constellation.executeAllMissions();

        assertEquals(1, satellite.getPhotosTaken());
    }

    @Test
    @DisplayName("Выполнение миссии CommunicationSatellite уменьшает уровень батареи")
    void givenActivatedCommunicationSatellite_whenExecutingMission_thenBatteryDecreases() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);
        CommunicationSatellite satellite = (CommunicationSatellite) communicationFactory.createSatelliteWithParameter(new CommunicationSatelliteParam("Связь-Тест", 0.85, 500.0));
        constellation.addSatellite(satellite);
        constellationRepository.create(CONSTELLATION_NAME, constellation);
        satellite.activate();
        double batteryBefore = satellite.getEnergy().getBatteryLevel();

        constellation.executeAllMissions();

        assertTrue(satellite.getEnergy().getBatteryLevel() < batteryBefore);
    }

    @Test
    @DisplayName("Полный жизненный цикл: создание → добавление → активация → миссия → удаление")
    void givenFullLifecycle_whenExecuted_thenAllStatesCorrect() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);
        constellationRepository.create(CONSTELLATION_NAME, constellation);
        assertNotNull(constellationRepository.get(CONSTELLATION_NAME));

        ImagingSatellite imagingSatellite = (ImagingSatellite) imagingFactory.createSatelliteWithParameter(new ImagingSatelliteParam("ДЗЗ-Тест", 0.9, 2.5));
        CommunicationSatellite commSatellite = (CommunicationSatellite) communicationFactory.createSatelliteWithParameter(new CommunicationSatelliteParam("Связь-Тест", 0.85, 500.0));
        constellation.addSatellite(imagingSatellite);
        constellation.addSatellite(commSatellite);
        assertEquals(2, constellationRepository.get(CONSTELLATION_NAME).getSatellites().size());

        imagingSatellite.activate();
        commSatellite.activate();
        assertTrue(imagingSatellite.getState().isActive());
        assertTrue(commSatellite.getState().isActive());

        constellation.executeAllMissions();
        assertEquals(1, imagingSatellite.getPhotosTaken());
        assertTrue(commSatellite.getEnergy().getBatteryLevel() < 0.85);

        constellationRepository.delete(CONSTELLATION_NAME);
        assertNull(constellationRepository.get(CONSTELLATION_NAME));
    }
}
