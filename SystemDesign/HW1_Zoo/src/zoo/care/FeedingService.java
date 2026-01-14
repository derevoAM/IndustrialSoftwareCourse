package zoo.care;

import zoo.animals.Animal;
import zoo.employees.Keeper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class FeedingService {
    private Map<Animal, FeedingSchedule> feedingSchedules;

    public FeedingService() {
        this.feedingSchedules = new HashMap<>();
    }

    public void createFeedingSchedule(Animal animal) {
        if (!(animal instanceof Feedable)) {
            System.out.println(animal.getName() + " кормить не нужно");
            return;
        }

        Feedable feedable = (Feedable) animal;

        List<LocalTime> times = generateFeedingTimes(feedable.getFeedingsPerDay());

        FeedingSchedule schedule = new FeedingSchedule(
                times,
                feedable.getPreferredFoodType(),
                feedable.getDailyFoodAmountKg() / feedable.getFeedingsPerDay()
        );

        feedingSchedules.put(animal, schedule);

        System.out.println("Расписание кормления создано для " + animal.getName());
    }

    private List<LocalTime> generateFeedingTimes(int feedingsPerDay) {
        List<LocalTime> times = new ArrayList<>();

        if (feedingsPerDay == 1) {
            times.add(LocalTime.of(12, 0));
        } else if (feedingsPerDay == 2) {
            times.add(LocalTime.of(9, 0));
            times.add(LocalTime.of(17, 0));
        } else if (feedingsPerDay == 3) {
            times.add(LocalTime.of(8, 0));
            times.add(LocalTime.of(13, 0));
            times.add(LocalTime.of(18, 0));
        }

        return times;
    }

    public FeedingSchedule getFeedingSchedule(Animal animal) {
        return feedingSchedules.get(animal);
    }

    public Map<Animal, FeedingSchedule> getAllSchedules() {
        return feedingSchedules;
    }

    public List<Animal> getAnimalsNeedingFeeding(List<Animal> animals, LocalTime currentTime) {
        List<Animal> result = new ArrayList<>();

        for (Animal animal : animals) {
            if (!(animal instanceof Feedable)) {
                continue;
            }

            FeedingSchedule schedule = feedingSchedules.get(animal);
            if (schedule == null) {
                continue;
            }

            if (schedule.getFeedingTimes().contains(currentTime)) {
                result.add(animal);
            }
        }

        return result;
    }
}