package zoo.care;

import java.time.LocalDate;

public class MedicalCheckup {
    private LocalDate date;
    private String veterinarianName;
    private HealthStatus status;
    private String notes;
    private String animal;
    private String animalName;

    public MedicalCheckup(LocalDate date, String veterinarianName, HealthStatus status, String notes, String animal, String animalName) {
        this.date = date;
        this.veterinarianName = veterinarianName;
        this.status = status;
        this.notes = notes;
        this.animal = animal;
        this.animalName = animalName;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getVeterinarianName() {
        return veterinarianName;
    }

    public HealthStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Медосмотр(" +
                "дата:" + date +
                ", животное:" + animal +
                ", имя:" + animalName +
                ", ветеринар:" + veterinarianName +
                ", состояние здоровья:" + status +
                ", комментарии:'" + notes + '\'' +
                ')';
    }
}