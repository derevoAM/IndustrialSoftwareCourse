package SatelliteConstellation.repository;

import SatelliteConstellation.domain.SatelliteConstellation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConstellationRepository {

    private Map<String, SatelliteConstellation> constellations = new HashMap<>();

    public void create(String id, SatelliteConstellation constellation) {
        constellations.put(id, constellation);
    }

    public SatelliteConstellation get(String id) {
        return constellations.get(id);
    }

    public Map<String, SatelliteConstellation> getConstellations() {
        return constellations;
    }

    public void delete(String id) {
        constellations.remove(id);
    }
}
