package SatelliteConstellation;

import SatelliteConstellation.domain.ImagingSatellite;
import SatelliteConstellation.domain.CommunicationSatellite;
import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.exception.AuthExceptions;
import SatelliteConstellation.param.CommunicationSatelliteParam;
import SatelliteConstellation.param.ImagingSatelliteParam;
import SatelliteConstellation.param.SatelliteParam;
import SatelliteConstellation.param.SatelliteType;
import SatelliteConstellation.service.SatelliteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SatelliteServiceTest {

    @Autowired
    private SatelliteService satelliteService;

    @Test
    @DisplayName("Сервис создаёт ImagingSatellite по ImagingSatelliteParam")
    void givenImagingParam_whenCreateSatellite_thenReturnsImagingSatellite() {
        SatelliteParam param = new ImagingSatelliteParam("ДЗЗ-1", 0.9, 2.5);

        Satellite satellite = satelliteService.createSatellite(param);

        assertInstanceOf(ImagingSatellite.class, satellite);
        assertEquals("ДЗЗ-1", satellite.getName());
        assertEquals(0.9, satellite.getEnergy().getBatteryLevel());
        assertEquals(2.5, ((ImagingSatellite) satellite).getResolution());
    }

    @Test
    @DisplayName("Сервис создаёт CommunicationSatellite по CommunicationSatelliteParam")
    void givenCommunicationParam_whenCreateSatellite_thenReturnsCommunicationSatellite() {
        SatelliteParam param = new CommunicationSatelliteParam("Связь-1", 0.85, 500.0);

        Satellite satellite = satelliteService.createSatellite(param);

        assertInstanceOf(CommunicationSatellite.class, satellite);
        assertEquals("Связь-1", satellite.getName());
        assertEquals(0.85, satellite.getEnergy().getBatteryLevel());
        assertEquals(500.0, ((CommunicationSatellite) satellite).getBandwidth());
    }

    @Test
    @DisplayName("Сервис бросает исключение если фабрика не найдена")
    void givenUnknownType_whenCreateSatellite_thenThrowsException() {
        SatelliteParam unknownParam = new SatelliteParam(SatelliteType.UNKNOWN, "Тест", 0.5) {};

        assertThrows(AuthExceptions.NoExistingFactoryException.class,
                () -> satelliteService.createSatellite(unknownParam));
    }
}
