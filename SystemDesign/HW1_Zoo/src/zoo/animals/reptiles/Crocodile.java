package zoo.animals.reptiles;

import zoo.animals.behaviour.Runnable;
import zoo.animals.behaviour.Swimmable;
import zoo.care.Feedable;
import zoo.care.Treatable;
import zoo.care.Cleanable;
import zoo.care.FoodType;

public class Crocodile extends Reptile implements Feedable, Treatable, Cleanable, Swimmable, Runnable {
    private double dailyFoodAmountKg;
    private int feedingsPerDay;

    public Crocodile(String name, int age, double weight, String scaleType, double dailyFoodAmountKg, int feedingsPerDay) {
        super(name, age, weight, scaleType);
        this.dailyFoodAmountKg = dailyFoodAmountKg;
        this.feedingsPerDay = feedingsPerDay;
    }

    @Override
    public String getSpecies() {
        return "Крокодил";
    }

    @Override
    public FoodType getPreferredFoodType() {
        return FoodType.MEAT;
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
        return 90;
    }

    @Override
    public int getCleaningFrequencyPerWeek() {
        return 4;
    }

    @Override
    public void swim() {
        System.out.println("Крокодил" + name + " плывет");
    }


    @Override
    public void run() {
        System.out.println("Крокодил" + name + " бежит");
    }
}