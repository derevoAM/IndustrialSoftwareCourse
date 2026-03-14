package SatelliteConstellation;

import SatelliteConstellation.domain.SatelliteConstellation;
import SatelliteConstellation.repository.ConstellationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConstellationRepositoryMockTest {

    private static final String CONSTELLATION_NAME = "Орбита-1";
    private static final String CONSTELLATION_NAME_2 = "Орбита-2";

    @Mock
    private ConstellationRepository constellationRepository;

    @Test
    @DisplayName("Добавление группировки в репозиторий")
    void givenConstellation_whenAddingToRepository_thenGetReturnsSavedConstellation() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);
        when(constellationRepository.get(CONSTELLATION_NAME)).thenReturn(constellation);

        constellationRepository.create(CONSTELLATION_NAME, constellation);
        SatelliteConstellation result = constellationRepository.get(CONSTELLATION_NAME);

        assertEquals(constellation, result);
        verify(constellationRepository).create(CONSTELLATION_NAME, constellation);
        verify(constellationRepository).get(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("get возвращает группировку по имени")
    void givenConstellationName_whenGettingConstellation_thenReturnConstellation() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);
        when(constellationRepository.get(CONSTELLATION_NAME)).thenReturn(constellation);

        SatelliteConstellation result = constellationRepository.get(CONSTELLATION_NAME);

        assertEquals(constellation, result);
        verify(constellationRepository).get(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("get возвращает null при отсутствии группировки в репозитории")
    void givenWrongConstellationName_whenGettingConstellation_thenReturnNull() {
        when(constellationRepository.get(CONSTELLATION_NAME)).thenReturn(null);

        assertNull(constellationRepository.get(CONSTELLATION_NAME));
        verify(constellationRepository).get(CONSTELLATION_NAME);
    }

    @Test
    @DisplayName("getConstellations возвращает все группировки")
    void givenConstellations_whenGettingAllConstellations_thenReturnConstellationCount() {
        SatelliteConstellation constellation = new SatelliteConstellation(CONSTELLATION_NAME);
        SatelliteConstellation constellation2 = new SatelliteConstellation(CONSTELLATION_NAME_2);
        Map<String, SatelliteConstellation> constellations = Map.of(
                CONSTELLATION_NAME, constellation,
                CONSTELLATION_NAME_2, constellation2
        );
        when(constellationRepository.getConstellations()).thenReturn(constellations);

        assertEquals(2, constellationRepository.getConstellations().size());
        verify(constellationRepository, times(1)).getConstellations();
    }

    @Test
    @DisplayName("delete удаляет группировку")
    void givenConstellation_whenDeleting_thenDeleteIsCalledAndGetReturnsNull() {
        constellationRepository.delete(CONSTELLATION_NAME);

        verify(constellationRepository).delete(CONSTELLATION_NAME);
        assertNull(constellationRepository.get(CONSTELLATION_NAME));
    }

    @Test
    @DisplayName("delete не вызывает ошибку при удалении несуществующей группировки")
    void givenWrongConstellationName_whenDeleting_thenNoException() {
        assertDoesNotThrow(() -> constellationRepository.delete("НесуществующаяОрбита"));
        verify(constellationRepository).delete("НесуществующаяОрбита");
    }
}
