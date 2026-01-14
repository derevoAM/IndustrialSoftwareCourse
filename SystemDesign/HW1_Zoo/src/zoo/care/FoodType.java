package zoo.care;

public enum FoodType {
    MEAT("Мясо"),
    SEEDS("Семена"),
    HAY("Сено"),
    FISH("Рыба");

    private final String food;

    FoodType(String food) {
        this.food = food;
    }

    public String getFood() {
        return food;
    }

    @Override
    public String toString() {
        return food;
    }
}