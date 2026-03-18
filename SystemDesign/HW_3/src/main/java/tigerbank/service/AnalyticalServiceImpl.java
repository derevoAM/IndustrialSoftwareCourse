package tigerbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tigerbank.domain.Category;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticalServiceImpl implements AnalyticalService {
    private final OperationService operationService;
    private final CategoryService categoryService;

    @Override
    public BigDecimal calculateDifference(String bankAccountId, LocalDate from, LocalDate to) {
        List<Operation> operations = operationService.findByBankAccountIdAndDateBetween(bankAccountId, from, to);
        return operations.stream()
                .map(o -> o.getType().equals(OperationType.EXPENSE) ? o.getAmount().negate() : o.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<Category, BigDecimal> groupByCategory(String bankAccountId, LocalDate from, LocalDate to) {
        List<Operation> operations = operationService.findByBankAccountIdAndDateBetween(bankAccountId, from, to);
        return operations.stream()
                .collect(Collectors.groupingBy(
                        o -> categoryService.findById(o.getCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Категория с id " + o.getCategoryId() + " не найдена")),
                        Collectors.reducing(BigDecimal.ZERO,
                                o -> o.getType() == OperationType.EXPENSE ? o.getAmount().negate() : o.getAmount(),
                                BigDecimal::add)
                ));
    }
}
