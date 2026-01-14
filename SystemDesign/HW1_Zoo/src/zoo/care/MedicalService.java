package zoo.care;

import zoo.animals.Animal;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class MedicalService {
    private Map<Animal, HealthStatus> healthStatuses;
    private Map<Animal, List<MedicalCheckup>> checkupHistory;

    public MedicalService() {
        this.healthStatuses = new HashMap<>();
        this.checkupHistory = new HashMap<>();
    }

    public void registerAnimal(Animal animal) {
        healthStatuses.put(animal, HealthStatus.HEALTHY);
        checkupHistory.put(animal, new ArrayList<>());
        System.out.println(animal.getName() + " получил медкарту");
    }

    public void recordCheckup(Animal animal, MedicalCheckup checkup) {
        if (!checkupHistory.containsKey(animal)) {
            registerAnimal(animal);
        }

        checkupHistory.get(animal).add(checkup);
        healthStatuses.put(animal, checkup.getStatus());
    }

    public void updateHealthStatus(Animal animal, HealthStatus status) {
        healthStatuses.put(animal, status);
        System.out.println(animal.getName() + " - состояние здоровья изменено: " + status);
    }

    public HealthStatus getHealthStatus(Animal animal) {
        return healthStatuses.get(animal);
    }

    public List<MedicalCheckup> getCheckupHistory(Animal animal) {
        return checkupHistory.get(animal);
    }

    public Map<Animal, List<MedicalCheckup>> getAllCheckupHistory() {
        return checkupHistory;
    }


    public Map<Animal, HealthStatus> getAllHealthStatuses() {
        return healthStatuses;
    }
}