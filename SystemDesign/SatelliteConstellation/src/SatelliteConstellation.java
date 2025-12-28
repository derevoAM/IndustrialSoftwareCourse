import java.util.List;
import java.util.ArrayList;

public class SatelliteConstellation {
    private String constellationName;
    private List<Satellite> satellites;

    public SatelliteConstellation(String constellationName){
        this.constellationName = constellationName;
        satellites = new ArrayList<Satellite>();
        System.out.println("Создана спутниковая группировка: " + constellationName);
    }

    public void addSatellite(Satellite satellite){
        satellites.add(satellite);
        System.out.println(satellite.getName() + " добавлен в группировку " + constellationName);
    }

    public void executeAllMissions(){
        for(Satellite satellite: satellites){
            satellite.performMission();
        }
    };

    public String getConstellationName(){
        return constellationName;
    }

    public List<Satellite> getSatellites(){
        return satellites;
    }

}