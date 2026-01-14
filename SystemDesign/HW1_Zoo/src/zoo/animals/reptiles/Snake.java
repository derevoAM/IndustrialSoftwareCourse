package zoo.animals.reptiles;

import zoo.animals.behaviour.Swimmable;
import zoo.care.Feedable;
import zoo.care.Treatable;
import zoo.care.Cleanable;
import zoo.care.FoodType;

public class Snake extends Reptile implements Feedable, Treatable, Cleanable, Swimmable {

    private double dailyFoodAmountKg;
    private int feedingsPerDay;

    public Snake(String name, int age, double weight, String scaleType, double dailyFoodAmountKg, int feedingsPerDay) {
        super(name, age, weight, scaleType);
        this.dailyFoodAmountKg = dailyFoodAmountKg;
        this.feedingsPerDay = feedingsPerDay;
    }

    @Override
    public String getSpecies() {
        return "Змея";
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
        return 60;
    }

    @Override
    public int getCleaningFrequencyPerWeek() {
        return 2;
    }


    @Override
    public void swim() {
        System.out.println("Змея" + name + " плывет");
    }

}