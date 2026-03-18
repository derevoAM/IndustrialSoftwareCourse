package tigerbank.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tigerbank.domain.BankAccount;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;
import tigerbank.repository.OperationRepository;
import tigerbank.service.BankAccountService;
import tigerbank.service.CategoryService;

@RequiredArgsConstructor
@Component
public class OperationValidator {
    private final BankAccountService bankAccountService;
    private final CategoryService categoryService;
    private final OperationRepository operationRepository;

    public void validate(String operationId, String bankAccountId, String categoryId, OperationType type)
    {
        BankAccount bankAccount = bankAccountService.getById(bankAccountId);
        Category category = categoryService.getById(categoryId);

        if (category.getType() != type) {
            throw new IllegalArgumentException("Тип операции не совпадает с типом категории");
        }

        if (operationRepository.findById(operationId).isPresent()) {
            throw new IllegalArgumentException("Операция с id " + operationId + " уже существует");
        }
    }
}
