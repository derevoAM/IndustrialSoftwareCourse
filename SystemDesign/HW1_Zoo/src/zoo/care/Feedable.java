package zoo.care;

public interface Feedable {
    FoodType getPreferredFoodType();
    double getDailyFoodAmountKg();
    int getFeedingsPerDay();
    void setDailyFoodAmountKg(double amount);
    void setFeedingsPerDay(int frequency);
}