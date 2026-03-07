package SatelliteConstellation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstellationRepositoryUnitTest {
    private static final String constellationName = "Орбита-1";
    private static final String constellationName2 = "Орбита-2";
    private ConstellationRepository constellationRepository;

    @BeforeEach
    void setUp()
    {
        constellationRepository = new ConstellationRepository();
    }


    @Test
    @DisplayName("Добавление группировки в репозиторий")
    void givenConstellation_whenAddingToRepository_thenReturnConstellationIsAdded()
    {
        SatelliteConstellation satelliteConstellation = new SatelliteConstellation(constellationName);
        constellationRepository.create(constellationName, satelliteConstellation);
        assertNotNull(constellationRepository.get(constellationName));
    }

    @Test
    @DisplayName("get возвращает группировку по имени")
    void givenConstellationName_whenGettingConstellation_thenReturnConstellation()
    {
        SatelliteConstellation satelliteConstellation = new SatelliteConstellation(constellationName);
        constellationRepository.create(constellationName, satelliteConstellation);
        assertEquals(satelliteConstellation, constellationRepository.get(constellationName));
    }

    @Test
    @DisplayName("get возвращает null при отсутствии группировки в репозитории")
    void givenWrongConstellationName_whenGettingConstellation_thenReturnNull()
    {
        assertNull(constellationRepository.get(constellationName));
    }

    @Test
    @DisplayName("getConstellations возвращает все группировки")
    void givenConstellations_whenGettingAllConstellations_thenReturnConstellationCount()
    {
        SatelliteConstellation satelliteConstellation = new SatelliteConstellation(constellationName);
        SatelliteConstellation satelliteConstellation2 = new SatelliteConstellation(constellationName2);
        constellationRepository.create(constellationName, satelliteConstellation);
        constellationRepository.create(constellationName2, satelliteConstellation2);
        assertEquals(2, constellationRepository.getConstellations().size());
    }

    @Test
    @DisplayName("delete удаляет группировку")
    void givenConstellation_whenDeleting_thenDeleted()
    {
        SatelliteConstellation satelliteConstellation = new SatelliteConstellation(constellationName);
        constellationRepository.create(constellationName, satelliteConstellation);
        constellationRepository.delete(constellationName);
        assertNull(constellationRepository.get(constellationName));
    }

    @Test
    @DisplayName("delete не вызывает ошибку при удалении несуществующей группировки")
    void givenWrongConstellationName_whenDeleting_thenNoException()
    {
        assertDoesNotThrow(() -> constellationRepository.delete(constellationName));
    }


}
