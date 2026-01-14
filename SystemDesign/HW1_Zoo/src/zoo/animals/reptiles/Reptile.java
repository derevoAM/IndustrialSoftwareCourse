package zoo.animals.reptiles;

import zoo.animals.Animal;

public abstract class Reptile extends Animal {
    protected String scaleType;

    public Reptile(String name, int age, double weight, String scaleType) {
        super(name, age, weight);
        this.scaleType = scaleType;
    }

    public String getScaleType() {
        return scaleType;
    }

    @Override
    public String toString() {
        return super.toString() + ", чешуя: " + scaleType;
    }
}