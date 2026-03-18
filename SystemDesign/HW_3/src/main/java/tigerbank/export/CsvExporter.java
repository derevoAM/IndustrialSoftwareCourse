package tigerbank.export;

import org.springframework.stereotype.Component;
import tigerbank.domain.BankAccount;
import tigerbank.domain.Category;
import tigerbank.domain.Operation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class CsvExporter implements DataExporter {

    @Override
    public void export(ExportData data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("=== ACCOUNTS ===");
            writer.newLine();
            writer.write("id,name,balance");
            writer.newLine();
            for (BankAccount a : data.getAccounts()) {
                writer.write(a.getId() + "," + a.getName() + "," + a.getBalance());
                writer.newLine();
            }

            writer.newLine();
            writer.write("=== CATEGORIES ===");
            writer.newLine();
            writer.write("id,type,name");
            writer.newLine();
            for (Category c : data.getCategories()) {
                writer.write(c.getId() + "," + c.getType() + "," + c.getName());
                writer.newLine();
            }

            writer.newLine();
            writer.write("=== OPERATIONS ===");
            writer.newLine();
            writer.write("id,type,bankAccountId,amount,date,description,categoryId");
            writer.newLine();
            for (Operation o : data.getOperations()) {
                writer.write(o.getId() + "," + o.getType() + "," + o.getBankAccountId() + ","
                        + o.getAmount() + "," + o.getDate() + ","
                        + (o.getDescription() != null ? o.getDescription() : "") + ","
                        + o.getCategoryId());
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при экспорте в CSV: " + filePath, e);
        }
    }
}
