package tigerbank.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    private String id;
    private String name;
    private BigDecimal balance;

    public BankAccount(String id, String name)
    {
        this.id = id;
        this.name = name;
        this.balance = BigDecimal.ZERO;
    }
}