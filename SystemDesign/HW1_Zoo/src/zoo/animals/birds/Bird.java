package zoo.animals.birds;

import zoo.animals.Animal;

public abstract class Bird extends Animal {
    protected double wingspan;
    protected String featherColor;

    public Bird(String name, int age, double weight, double wingspan, String featherColor) {
        super(name, age, weight);
        this.wingspan = wingspan;
        this.featherColor = featherColor;
    }

    public double getWingspan() {
        return wingspan;
    }

    public String getFeatherColor() {
        return featherColor;
    }

    @Override
    public String toString() {
        return super.toString() + ", размах крыла: " + wingspan + "м, перья: " + featherColor;
    }
}