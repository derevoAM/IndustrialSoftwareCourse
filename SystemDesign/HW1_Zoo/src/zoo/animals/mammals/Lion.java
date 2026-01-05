package zoo.animals.mammals;

import zoo.animals.behaviour.Runnable;
import zoo.care.Feedable;
import zoo.care.Treatable;
import zoo.care.Cleanable;
import zoo.care.FoodType;

public class Lion extends Mammal implements Feedable, Treatable, Cleanable, Runnable {
    private double dailyFoodAmountKg;
    private int feedingsPerDay;

    public Lion(String name, int age, double weight, String furColor, double dailyFoodAmountKg, int feedingsPerDay) {
        super(name, age, weight, furColor);
        this.dailyFoodAmountKg = dailyFoodAmountKg;
        this.feedingsPerDay = feedingsPerDay;
    }

    @Override
    public String getSpecies() {
        return "Лев";
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
        return false;
    }

    @Override
    public int getCheckupIntervalDays() {
        return 30;
    }


    @Override
    public int getCleaningFrequencyPerWeek() {
        return 7;
    }

    @Override
    public void run() {
        System.out.println("Лев " + name + " бежит");
    }
}