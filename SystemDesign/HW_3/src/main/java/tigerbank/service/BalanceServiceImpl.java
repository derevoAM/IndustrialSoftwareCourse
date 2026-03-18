package tigerbank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tigerbank.domain.BankAccount;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;
import tigerbank.repository.OperationRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BankAccountService bankAccountService;
    private final OperationRepository operationRepository;

    @Override
    public void recalculate(String bankAccountId) {
        BankAccount account = bankAccountService.getById(bankAccountId);
        List<Operation> operations = operationRepository.findByBankAccountId(bankAccountId);
        BigDecimal balance = operations.stream()
                .map(o -> o.getType().equals(OperationType.EXPENSE) ? o.getAmount().negate() : o.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        account.setBalance(balance);
    }
}
