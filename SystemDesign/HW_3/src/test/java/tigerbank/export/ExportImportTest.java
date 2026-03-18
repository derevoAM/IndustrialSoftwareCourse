package tigerbank.export;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tigerbank.domain.BankAccount;
import tigerbank.domain.Category;
import tigerbank.domain.Operation;
import tigerbank.domain.OperationType;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExportImportTest {

    private ExportData data;

    @BeforeEach
    void setUp() {
        List<BankAccount> accounts = List.of(
                new BankAccount("acc1", "Основной", new BigDecimal("1000")),
                new BankAccount("acc2", "Накопительный", new BigDecimal("5000"))
        );
        List<Category> categories = List.of(
                new Category("cat1", OperationType.INCOME, "Зарплата"),
                new Category("cat2", OperationType.EXPENSE, "Еда")
        );
        List<Operation> operations = List.of(
                new Operation("op1", OperationType.INCOME, "acc1",
                        new BigDecimal("3000"), LocalDate.of(2024, 1, 15), null, "cat1"),
                new Operation("op2", OperationType.EXPENSE, "acc1",
                        new BigDecimal("1200"), LocalDate.of(2024, 1, 20), "Продукты", "cat2")
        );
        data = new ExportData(accounts, categories, operations);
    }

    @Test
    @DisplayName("JSON экспорт и импорт восстанавливают данные без потерь")
    void givenExportData_whenJsonExportAndImport_thenDataRestored(@TempDir Path tempDir) {
        String path = tempDir.resolve("data.json").toString();
        new JsonExporter().export(data, path);
        ExportData imported = new JsonImporter().importData(path);
        assertExportDataEquals(data, imported);
    }

    @Test
    @DisplayName("CSV экспорт и импорт восстанавливают данные без потерь")
    void givenExportData_whenCsvExportAndImport_thenDataRestored(@TempDir Path tempDir) {
        String path = tempDir.resolve("data.csv").toString();
        new CsvExporter().export(data, path);
        ExportData imported = new CsvImporter().importData(path);
        assertExportDataEquals(data, imported);
    }

    @Test
    @DisplayName("YAML экспорт и импорт восстанавливают данные без потерь")
    void givenExportData_whenYamlExportAndImport_thenDataRestored(@TempDir Path tempDir) {
        String path = tempDir.resolve("data.yaml").toString();
        new YamlExporter().export(data, path);
        ExportData imported = new YamlImporter().importData(path);
        assertExportDataEquals(data, imported);
    }

    @Test
    @DisplayName("JSON импорт сохраняет null в описании операции")
    void givenOperationWithNullDescription_whenJsonExportAndImport_thenDescriptionRemainsNull(@TempDir Path tempDir) {
        String path = tempDir.resolve("data.json").toString();
        new JsonExporter().export(data, path);
        ExportData imported = new JsonImporter().importData(path);
        Operation op = imported.getOperations().stream()
                .filter(o -> o.getId().equals("op1"))
                .findFirst().orElseThrow();
        assertNull(op.getDescription());
    }

    @Test
    @DisplayName("CSV импорт сохраняет описание операции")
    void givenOperationWithDescription_whenCsvExportAndImport_thenDescriptionPreserved(@TempDir Path tempDir) {
        String path = tempDir.resolve("data.csv").toString();
        new CsvExporter().export(data, path);
        ExportData imported = new CsvImporter().importData(path);
        Operation op = imported.getOperations().stream()
                .filter(o -> o.getId().equals("op2"))
                .findFirst().orElseThrow();
        assertEquals("Продукты", op.getDescription());
    }

    @Test
    @DisplayName("JSON экспорт бросает исключение если путь недоступен")
    void givenInvalidPath_whenJsonExport_thenThrowsException() {
        assertThrows(RuntimeException.class, () ->
                new JsonExporter().export(data, "/nonexistent/path/data.json"));
    }

    @Test
    @DisplayName("JSON импорт бросает исключение если файл не существует")
    void givenNonExistentFile_whenJsonImport_thenThrowsException() {
        assertThrows(RuntimeException.class, () ->
                new JsonImporter().importData("/nonexistent/path/data.json"));
    }

    private void assertExportDataEquals(ExportData expected, ExportData actual) {
        assertEquals(expected.getAccounts().size(), actual.getAccounts().size());
        assertEquals(expected.getCategories().size(), actual.getCategories().size());
        assertEquals(expected.getOperations().size(), actual.getOperations().size());

        for (int i = 0; i < expected.getAccounts().size(); i++) {
            BankAccount e = expected.getAccounts().get(i);
            BankAccount a = actual.getAccounts().get(i);
            assertEquals(e.getId(), a.getId());
            assertEquals(e.getName(), a.getName());
            assertEquals(0, e.getBalance().compareTo(a.getBalance()));
        }

        for (int i = 0; i < expected.getCategories().size(); i++) {
            Category e = expected.getCategories().get(i);
            Category a = actual.getCategories().get(i);
            assertEquals(e.getId(), a.getId());
            assertEquals(e.getName(), a.getName());
            assertEquals(e.getType(), a.getType());
        }

        for (int i = 0; i < expected.getOperations().size(); i++) {
            Operation e = expected.getOperations().get(i);
            Operation a = actual.getOperations().get(i);
            assertEquals(e.getId(), a.getId());
            assertEquals(e.getType(), a.getType());
            assertEquals(e.getBankAccountId(), a.getBankAccountId());
            assertEquals(0, e.getAmount().compareTo(a.getAmount()));
            assertEquals(e.getDate(), a.getDate());
            assertEquals(e.getDescription(), a.getDescription());
            assertEquals(e.getCategoryId(), a.getCategoryId());
        }
    }
}
