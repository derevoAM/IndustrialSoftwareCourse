package zoo.employees;

import zoo.animals.Animal;
import zoo.cages.Enclosure;
import zoo.care.Feedable;
import zoo.care.Cleanable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Keeper implements Employee {
    private String name;
    private String assignedSection;
    private List<Enclosure> assignedEnclosures;

    public Keeper(String name, String assignedSection) {
        this.name = name;
        this.assignedSection = assignedSection;
        this.assignedEnclosures = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPosition() {
        return "Смотритель (" + assignedSection + ")";
    }


    public void assignEnclosure(Enclosure enclosure) {
        if (!assignedEnclosures.contains(enclosure)) {
            assignedEnclosures.add(enclosure);
            System.out.println(name + " назначен на вольер " + enclosure.getId());
        }
    }

    public void unassignEnclosure(Enclosure enclosure) {
        assignedEnclosures.remove(enclosure);
    }

    public void feedAnimal(Animal animal) {
        Feedable feedable = (Feedable) animal;
        double amount = feedable.getDailyFoodAmountKg() / feedable.getFeedingsPerDay();

        System.out.println(name + " покормил " + animal.getName() +
                " (" + amount + " кг " + feedable.getPreferredFoodType() + ")");
    }

    public void cleanEnclosure(Enclosure enclosure) {
        System.out.println(name + " убрал вольер " + enclosure.getId());
    }

    public String getAssignedSection() {
        return assignedSection;
    }

    public List<Enclosure> getAssignedEnclosures() {
        return new ArrayList<>(assignedEnclosures);
    }
}