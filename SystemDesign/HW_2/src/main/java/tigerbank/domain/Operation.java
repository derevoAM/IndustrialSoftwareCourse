package tigerbank.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Operation{
    private String id;
    private OperationType type;
    private String bankAccountId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String categoryId;

    public Operation(String id, OperationType type, String bankAccountId,
                     BigDecimal amount, LocalDate date, String categoryId) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = null;
        this.categoryId = categoryId;
    }
}