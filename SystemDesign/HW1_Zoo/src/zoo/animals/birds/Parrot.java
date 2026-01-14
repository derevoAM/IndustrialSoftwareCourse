package zoo.animals.birds;

import zoo.animals.behaviour.Flyable;
import zoo.care.Feedable;
import zoo.care.Treatable;
import zoo.care.Cleanable;
import zoo.care.FoodType;

public class Parrot extends Bird implements Feedable, Treatable, Cleanable, Flyable {

    private double dailyFoodAmountKg;
    private int feedingsPerDay;

    public Parrot(String name, int age, double weight, double wingspan, String featherColor, double dailyFoodAmountKg, int feedingsPerDay) {
        super(name, age, weight, wingspan, featherColor);
        this.dailyFoodAmountKg = dailyFoodAmountKg;
        this.feedingsPerDay = feedingsPerDay;
    }

    @Override
    public String getSpecies() {
        return "Попугай";
    }

    @Override
    public FoodType getPreferredFoodType() {
        return FoodType.SEEDS;
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
        return false;
    }

    @Override
    public int getCheckupIntervalDays() {
        return 30;
    }

    @Override
    public int getCleaningFrequencyPerWeek() {
        return 5;
    }

    @Override
    public void fly() {
        System.out.println("Попугай " + name + " летит");
    }
}