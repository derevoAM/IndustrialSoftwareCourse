package zoo.employees;

import zoo.animals.Animal;
import zoo.care.Treatable;
import zoo.care.HealthStatus;
import zoo.care.MedicalCheckup;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Veterinarian implements Employee {
    private String name;
    private String specialization;
    private List<Animal> scheduledCheckups;

    public Veterinarian(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
        this.scheduledCheckups = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPosition() {
        return "Ветеринар (" + specialization + ")";
    }

    public void scheduleCheckup(Animal animal) {
        if (!scheduledCheckups.contains(animal)) {
            scheduledCheckups.add(animal);
            System.out.println("Осмотр запланирован для " + animal.getName() + " (доктор " + name + ")");
        }
    }

    public MedicalCheckup examineAnimal(Animal animal) {
        if (!(animal instanceof Treatable)) {
            System.out.println(animal.getName() + " не может быть осмотрен");
            return null;
        }

        Treatable treatable = (Treatable) animal;
        System.out.println("Доктор " + name + " осматривает " + animal.getName());

        HealthStatus status = HealthStatus.HEALTHY;
        String notes = "Плановый осмотр выполнен";

        if (treatable.requiresSpecialCare()) {
            notes = "Животное требует особого ухода - необходим мониторинг";
            System.out.println("Отмечен особый уход");
        }

        MedicalCheckup checkup = new MedicalCheckup(LocalDate.now(), this.name, status, notes, animal.getSpecies(), animal.getName());
        System.out.println("Статус: " + status);

        return checkup;
    }

    public String getSpecialization() {
        return specialization;
    }

    public List<Animal> getScheduledCheckups() {
        return new ArrayList<>(scheduledCheckups);
    }
}