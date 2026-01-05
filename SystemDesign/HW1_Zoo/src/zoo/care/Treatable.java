package zoo.care;

public interface Treatable {
    boolean requiresSpecialCare();
    int getCheckupIntervalDays();
}