package zoo.animals;

public abstract class Animal {
    protected String name;
    protected int age;
    protected double weight;

    public Animal(String name, int age, double weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public abstract String getSpecies();

    @Override
    public String toString() {
        return getSpecies() + " '" + name + "' (возраст: " + age + ", вес: " + weight + "кг)";
    }
}