package zoo.cages;

import zoo.animals.Animal;
import java.util.ArrayList;
import java.util.List;

public class Enclosure {
    private String id;
    private EnclosureType type;
    private List<Animal> animals;

    public Enclosure(String id, EnclosureType type) {
        this.id = id;
        this.type = type;
        this.animals = new ArrayList<>();
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public String getId() {
        return id;
    }

    public EnclosureType getType() {
        return type;
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    @Override
    public String toString() {
        return "Ограждение(id:'" + id + "', тип:" + type + ", количество животных:" + animals.size() + ")";
    }
}