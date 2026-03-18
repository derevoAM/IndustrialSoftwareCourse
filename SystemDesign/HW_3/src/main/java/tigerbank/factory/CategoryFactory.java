package tigerbank.factory;

import org.springframework.stereotype.Component;
import tigerbank.domain.Category;
import tigerbank.domain.OperationType;

@Component
public class CategoryFactory {

    public Category create(String id, OperationType type, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id не может быть пустым");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name не может быть пустым");
        if (type == null) throw new IllegalArgumentException("type не может быть null");
        return new Category(id, type, name);
    }
}
