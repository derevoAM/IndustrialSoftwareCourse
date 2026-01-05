package zoo;

import zoo.animals.Animal;
import zoo.employees.Employee;
import zoo.cages.Enclosure;
import java.util.ArrayList;
import java.util.List;

public class Zoo {
    private String name;
    private String address;
    private List<Animal> animals;
    private List<Employee> employees;
    private List<Enclosure> enclosures;

    public Zoo(String name, String address) {
        this.name = name;
        this.address = address;
        this.animals = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.enclosures = new ArrayList<>();
        System.out.println("Создан " + name + " по адресу " + address);
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        System.out.println(animal.getName() + " " + animal.getSpecies() + " добавлен");
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
        System.out.println(animal.getName() + " убран");
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        System.out.println(employee.getName() + " нанят на должность " + employee.getPosition());
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        System.out.println(employee.getName() + " уволен");
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public void addEnclosure(Enclosure enclosure) {
        enclosures.add(enclosure);
        System.out.println("Ограждение " + enclosure.getId() + " добавлено");
    }

    public List<Enclosure> getEnclosures() {
        return new ArrayList<>(enclosures);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}