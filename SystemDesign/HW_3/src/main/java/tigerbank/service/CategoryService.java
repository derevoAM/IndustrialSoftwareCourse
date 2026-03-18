package tigerbank.service;

import tigerbank.domain.BankAccount;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    void create(String id, OperationType type, String name);

    void updateName(String id, String newName);

    void delete(String id);

    Optional<Category> findById(String id);

    Category getById(String id);


    List<Category> findAll();

    List<Category> findByType(OperationType type);
}
