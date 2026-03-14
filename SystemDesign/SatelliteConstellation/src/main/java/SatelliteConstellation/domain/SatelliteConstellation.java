package SatelliteConstellation.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Setter
@Getter
public class SatelliteConstellation {
    private String constellationName;
    private List<Satellite> satellites;

    public SatelliteConstellation(String constellationName) {
        this.constellationName = constellationName;
        satellites = new ArrayList<Satellite>();
        System.out.println("Создана спутниковая группировка: " + constellationName);
    }

    public void addSatellite(Satellite satellite) {
        if (satellite != null && !satellites.contains(satellite)) {
            satellites.add(satellite);
            System.out.println(satellite.getName() + " добавлен в группировку '" + constellationName + "'");
        }
    }

    public void executeAllMissions() {
        System.out.println("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ " + constellationName.toUpperCase());
        System.out.println("==================================================");
        for (Satellite satellite : satellites) {
            satellite.performMission();
            satellite.updateState();
        }
    }

    @Override
    public String toString() {
        return "SatelliteConstellation{constellationName='" + constellationName + "', satellites=" + satellites + "}";
    }
}
