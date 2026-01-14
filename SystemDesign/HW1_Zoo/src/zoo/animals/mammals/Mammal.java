package zoo.animals.mammals;

import zoo.animals.Animal;

public abstract class Mammal extends Animal {
    protected String furColor;

    public Mammal(String name, int age, double weight, String furColor) {
        super(name, age, weight);
        this.furColor = furColor;
    }

    public String getFurColor() {
        return furColor;
    }


    @Override
    public String toString() {
        return super.toString() + ", шерсть: " + furColor;
    }
}