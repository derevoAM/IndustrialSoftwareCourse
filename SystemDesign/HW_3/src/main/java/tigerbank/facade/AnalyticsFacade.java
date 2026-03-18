package tigerbank.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tigerbank.domain.BankAccount;
import tigerbank.domain.Category;
import tigerbank.service.AnalyticalService;
import tigerbank.service.BankAccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AnalyticsFacade {
    private final AnalyticalService analyticalService;
    private final BankAccountService bankAccountService;

    public BigDecimal calculateDifference(String bankAccountId, LocalDate from, LocalDate to) {
        return analyticalService.calculateDifference(bankAccountId, from, to);
    }

    public Map<Category, BigDecimal> groupByCategory(String bankAccountId, LocalDate from, LocalDate to) {
        return analyticalService.groupByCategory(bankAccountId, from, to);
    }

    public AnalyticsReport getFullReport(String bankAccountId, LocalDate from, LocalDate to) {
        BankAccount account = bankAccountService.getById(bankAccountId);
        BigDecimal difference = analyticalService.calculateDifference(bankAccountId, from, to);
        Map<Category, BigDecimal> byCategory = analyticalService.groupByCategory(bankAccountId, from, to);
        return new AnalyticsReport(account, difference, byCategory, from, to);
    }

    public record AnalyticsReport(
            BankAccount account,
            BigDecimal difference,
            Map<Category, BigDecimal> byCategory,
            LocalDate from,
            LocalDate to
    ) {}
}
