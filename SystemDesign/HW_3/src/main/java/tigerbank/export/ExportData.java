package tigerbank.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tigerbank.domain.BankAccount;
import tigerbank.domain.Category;
import tigerbank.domain.Operation;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExportData {
    private List<BankAccount> accounts;
    private List<Category> categories;
    private List<Operation> operations;
}
