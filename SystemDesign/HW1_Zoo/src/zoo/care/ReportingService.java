package zoo.care;

import zoo.animals.Animal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ReportingService {

    public void generateAnimalCountReport(List<Animal> animals) {
        System.out.println("\n=== ОТЧЕТ ПО ЖИВОТНЫМ ===");
        System.out.println("Всего животных: " + animals.size());

        Map<String, Integer> speciesCount = new HashMap<>();
        for (Animal animal : animals) {
            String species = animal.getSpecies();
            speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
        }

        System.out.println("По видам:");
        for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("==========================\n");
    }

    public void generateFeedingReport(FeedingService feedingService) {
        System.out.println("\n=== ОТЧЕТ ПО КОРМЛЕНИЮ ===");
        Map<Animal, FeedingSchedule> schedules = feedingService.getAllSchedules();

        for (Map.Entry<Animal, FeedingSchedule> entry : schedules.entrySet()) {
            Animal animal = entry.getKey();
            FeedingSchedule schedule = entry.getValue();

            System.out.println(animal.getName() + " (" + animal.getSpecies() + "):");
            System.out.println("  Еда: " + schedule.getFoodType());
            System.out.println("  Порция: " + schedule.getPortionSizeKg() + " кг");
            System.out.println("  Время: " + schedule.getFeedingTimes());
        }
        System.out.println("==========================\n");
    }

    public void generateMedicalReport(MedicalService medicalService) {
        System.out.println("\n=== МЕДИЦИНСКИЙ ОТЧЕТ ===");
        Map<Animal, HealthStatus> statuses = medicalService.getAllHealthStatuses();

        for (Map.Entry<Animal, HealthStatus> entry : statuses.entrySet()) {
            System.out.println(entry.getKey().getName() + " (" +
                    entry.getKey().getSpecies() + "): " + entry.getValue());
        }

        System.out.println("\nИСТОРИЯ ОСМОТРОВ:\n");
        for (Map.Entry<Animal, List<MedicalCheckup>> entry : medicalService.getAllCheckupHistory().entrySet()) {
            Animal animal = entry.getKey();
            List<MedicalCheckup> checkups = entry.getValue();

            for (MedicalCheckup checkup : checkups) {
                System.out.println("  " + checkup);
            }
        }

        System.out.println("=========================\n");
    }
}