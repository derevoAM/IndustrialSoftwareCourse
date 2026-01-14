package zoo.care;

import java.time.LocalTime;
import java.util.List;

public class FeedingSchedule {
    private List<LocalTime> feedingTimes;
    private FoodType foodType;
    private double portionSizeKg;

    public FeedingSchedule(List<LocalTime> feedingTimes, FoodType foodType, double portionSizeKg) {
        this.feedingTimes = feedingTimes;
        this.foodType = foodType;
        this.portionSizeKg = portionSizeKg;
    }

    public List<LocalTime> getFeedingTimes() {
        return feedingTimes;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public double getPortionSizeKg() {
        return portionSizeKg;
    }

    @Override
    public String toString() {
        return "Расписание кормления(" +
                "время: " + feedingTimes +
                ", еда: " + foodType +
                ", количество: " + portionSizeKg +
                "кг)";
    }
}