package zoo.animals.mammals;

import zoo.animals.behaviour.Runnable;
import zoo.animals.behaviour.Swimmable;
import zoo.care.Feedable;
import zoo.care.Treatable;
import zoo.care.Cleanable;
import zoo.care.FoodType;

public class Elephant extends Mammal implements Feedable, Treatable, Cleanable, Swimmable, Runnable {
    private double dailyFoodAmountKg;
    private int feedingsPerDay;

    public Elephant(String name, int age, double weight, String furColor, double dailyFoodAmountKg, int feedingsPerDay) {
        super(name, age, weight, furColor);
        this.dailyFoodAmountKg = dailyFoodAmountKg;
        this.feedingsPerDay = feedingsPerDay;
    }

    @Override
    public String getSpecies() {
        return "Cлон";
    }

    @Override
    public FoodType getPreferredFoodType() {
        return FoodType.HAY;
    }

    @Override
    public double getDailyFoodAmountKg() {
        return dailyFoodAmountKg;
    }

    @Override
    public int getFeedingsPerDay() {
        return feedingsPerDay;
    }

    @Override
    public void setDailyFoodAmountKg(double amount) {
        dailyFoodAmountKg = amount;
    }

    @Override
    public void setFeedingsPerDay(int frequency) {
        feedingsPerDay = frequency;
    }

    @Override
    public boolean requiresSpecialCare() {
        return true;
    }

    @Override
    public int getCheckupIntervalDays() {
        return 45;
    }


    @Override
    public int getCleaningFrequencyPerWeek() {
        return 7;
    }

    @Override
    public void swim() {
        System.out.println("Слон " + name + " плывет");
    }

    @Override
    public void run() {
        System.out.println("Слон " + name + " бежит");
    }
}