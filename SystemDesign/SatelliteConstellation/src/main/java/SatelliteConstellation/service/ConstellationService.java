package SatelliteConstellation.service;

import SatelliteConstellation.domain.Satellite;
import SatelliteConstellation.domain.SatelliteConstellation;
import SatelliteConstellation.repository.ConstellationRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ConstellationService {

    private ConstellationRepository constellationRepository;

    public ConstellationService(ConstellationRepository constellationRepository) {
        this.constellationRepository = constellationRepository;
    }

    public void createAndSaveConstellation(String name) {
        if (constellationRepository.get(name) == null) {
            SatelliteConstellation constellation = new SatelliteConstellation(name);
            constellationRepository.create(name, constellation);
            System.out.println("Сохранена группировка: " + name);
        } else System.out.println("Группировка с именем " + name + " уже существует");
    }

    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        if (constellationRepository.get(constellationName) != null && satellite != null) {
            SatelliteConstellation constellation = constellationRepository.get(constellationName);
            boolean isNameTaken = constellation.getSatellites().stream()
                            .anyMatch(f -> Objects.equals(f.getName(), satellite.getName()));
            if(isNameTaken) {
                throw new RuntimeException("Спутник с именем " + satellite.getName() + " уже существует в группировке " + constellationName);
            }

            constellation.addSatellite(satellite);
            System.out.println("Добавлен спутник " + satellite.getName() + " в группировку " + constellationName);
        } else System.out.println("Ошибка добавления спутника в группировку " + constellationName);
    }

    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = constellationRepository.get(constellationName);
        if (constellation != null) {
            System.out.println("\n=== АКТИВАЦИЯ СПУТНИКОВ В ГРУППИРОВКЕ: " + constellationName + " ===");
            for (Satellite satellite : constellation.getSatellites()) {
                satellite.activate();
            }
        }
    }

    public void executeConstellationMission(String constellationName) {
        SatelliteConstellation constellation = constellationRepository.get(constellationName);
        if (constellation != null) {
            System.out.println("\n=== ВЫПОЛНЕНИЕ МИССИЙ ДЛЯ ГРУППИРОВКИ: " + constellationName + " ===");
            constellation.executeAllMissions();
        }
    }

    public void showConstellationStatus(String constellationName) {
        SatelliteConstellation constellation = constellationRepository.get(constellationName);
        if (constellation != null) {
            System.out.println("\n=== СТАТУС ГРУППИРОВКИ: " + constellationName + " ===");
            System.out.println("Количество спутников: " + constellation.getSatellites().size());
            for (Satellite satellite : constellation.getSatellites()) {
                System.out.println(satellite.getState().toString());
            }
        }
    }

    public boolean isConstellationInRepository(String name)
    {
        return constellationRepository.get(name) != null;
    }

}
