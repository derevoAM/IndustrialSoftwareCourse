package tigerbank.service;

import tigerbank.domain.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface AnalyticsService {
    BigDecimal calculateDifference(String bankAccountId, LocalDate from, LocalDate to);
    Map<Category, BigDecimal> groupByCategory(String bankAccountId, LocalDate from, LocalDate to);
}
