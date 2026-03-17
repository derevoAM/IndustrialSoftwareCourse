package tigerbank.export;

import org.springframework.stereotype.Component;
import tigerbank.domain.BankAccount;
import tigerbank.domain.Category;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvImporter implements DataImporter {

    @Override
    public ExportData importData(String filePath) {
        List<BankAccount> accounts = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String section = null;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                if (line.startsWith("===")) {
                    section = line;
                    headerSkipped = false;
                    continue;
                }

                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] parts = line.split(",", -1);

                if ("=== ACCOUNTS ===".equals(section)) {
                    accounts.add(new BankAccount(parts[0], parts[1], new BigDecimal(parts[2])));
                } else if ("=== CATEGORIES ===".equals(section)) {
                    categories.add(new Category(parts[0], OperationType.valueOf(parts[1]), parts[2]));
                } else if ("=== OPERATIONS ===".equals(section)) {
                    String description = parts[5].isBlank() ? null : parts[5];
                    operations.add(new Operation(parts[0], OperationType.valueOf(parts[1]),
                            parts[2], new BigDecimal(parts[3]), LocalDate.parse(parts[4]),
                            description, parts[6]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при импорте из CSV: " + filePath, e);
        }

        return new ExportData(accounts, categories, operations);
    }
}
