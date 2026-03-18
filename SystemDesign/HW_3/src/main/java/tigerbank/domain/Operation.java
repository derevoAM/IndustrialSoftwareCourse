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

}