package tigerbank;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tigerbank.domain.OperationType;
import tigerbank.export.DataExporter;
import tigerbank.export.DataImporter;
import tigerbank.export.ExportData;
import tigerbank.export.JsonExporter;
import tigerbank.export.JsonImporter;
import tigerbank.export.CsvExporter;
import tigerbank.export.CsvImporter;
import tigerbank.export.YamlExporter;
import tigerbank.export.YamlImporter;
import tigerbank.service.AnalyticalService;
import tigerbank.service.BankAccountService;
import tigerbank.service.CategoryService;
import tigerbank.service.OperationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static BankAccountService bankAccountService;
    private static CategoryService categoryService;
    private static OperationService operationService;
    private static AnalyticalService analyticalService;
    private static JsonExporter jsonExporter;
    private static JsonImporter jsonImporter;
    private static CsvExporter csvExporter;
    private static CsvImporter csvImporter;
    private static YamlExporter yamlExporter;
    private static YamlImporter yamlImporter;

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("tigerbank");
        bankAccountService = context.getBean(BankAccountService.class);
        categoryService = context.getBean(CategoryService.class);
        operationService = context.getBean(OperationService.class);
        analyticalService = context.getBean(AnalyticalService.class);
        jsonExporter = context.getBean(JsonExporter.class);
        jsonImporter = context.getBean(JsonImporter.class);
        csvExporter = context.getBean(CsvExporter.class);
        csvImporter = context.getBean(CsvImporter.class);
        yamlExporter = context.getBean(YamlExporter.class);
        yamlImporter = context.getBean(YamlImporter.class);

        while (true) {
            System.out.println("\n=== TigerBank ===");
            System.out.println("1. Счета");
            System.out.println("2. Категории");
            System.out.println("3. Операции");
            System.out.println("4. Аналитика");
            System.out.println("5. Экспорт / Импорт");
            System.out.println("0. Выход");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> accountsMenu();
                case "2" -> categoriesMenu();
                case "3" -> operationsMenu();
                case "4" -> analyticsMenu();
                case "5" -> exportMenu();
                case "0" -> {
                    System.out.println("До свидания!");
                    context.close();
                    return;
                }
                default -> System.out.println("Неизвестная команда");
            }
        }
    }

    private static void accountsMenu() {
        System.out.println("\n--- Счета ---");
        System.out.println("1. Создать счёт");
        System.out.println("2. Показать все счета");
        System.out.println("3. Найти счёт по id");
        System.out.println("4. Переименовать счёт");
        System.out.println("5. Удалить счёт");
        System.out.print("> ");

        switch (scanner.nextLine().trim()) {
            case "1" -> {
                System.out.print("id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Название: ");
                String name = scanner.nextLine().trim();
                System.out.print("Начальный баланс (Enter — 0): ");
                String bal = scanner.nextLine().trim();
                try {
                    if (bal.isBlank()) {
                        bankAccountService.create(id, name);
                    } else {
                        bankAccountService.create(id, name, new BigDecimal(bal));
                    }
                    System.out.println("Счёт создан");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "2" -> bankAccountService.findAll()
                    .forEach(a -> System.out.println(a.getId() + " | " + a.getName() + " | " + a.getBalance()));
            case "3" -> {
                System.out.print("id: ");
                bankAccountService.findById(scanner.nextLine().trim())
                        .ifPresentOrElse(
                                a -> System.out.println(a.getId() + " | " + a.getName() + " | " + a.getBalance()),
                                () -> System.out.println("Не найден"));
            }
            case "4" -> {
                System.out.print("id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Новое название: ");
                String name = scanner.nextLine().trim();
                try {
                    bankAccountService.updateName(id, name);
                    System.out.println("Обновлено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "5" -> {
                System.out.print("id: ");
                try {
                    bankAccountService.delete(scanner.nextLine().trim());
                    System.out.println("Удалено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            default -> System.out.println("Неизвестная команда");
        }
    }

    private static void categoriesMenu() {
        System.out.println("\n--- Категории ---");
        System.out.println("1. Создать категорию");
        System.out.println("2. Показать все категории");
        System.out.println("3. Найти по id");
        System.out.println("4. Переименовать категорию");
        System.out.println("5. Удалить категорию");
        System.out.print("> ");

        switch (scanner.nextLine().trim()) {
            case "1" -> {
                System.out.print("id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Тип (INCOME/EXPENSE): ");
                String type = scanner.nextLine().trim();
                System.out.print("Название: ");
                String name = scanner.nextLine().trim();
                try {
                    categoryService.create(id, OperationType.valueOf(type), name);
                    System.out.println("Категория создана");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "2" -> categoryService.findAll()
                    .forEach(c -> System.out.println(c.getId() + " | " + c.getType() + " | " + c.getName()));
            case "3" -> {
                System.out.print("id: ");
                categoryService.findById(scanner.nextLine().trim())
                        .ifPresentOrElse(
                                c -> System.out.println(c.getId() + " | " + c.getType() + " | " + c.getName()),
                                () -> System.out.println("Не найдена"));
            }
            case "4" -> {
                System.out.print("id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Новое название: ");
                String name = scanner.nextLine().trim();
                try {
                    categoryService.updateName(id, name);
                    System.out.println("Обновлено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "5" -> {
                System.out.print("id: ");
                try {
                    categoryService.delete(scanner.nextLine().trim());
                    System.out.println("Удалено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            default -> System.out.println("Неизвестная команда");
        }
    }

    private static void operationsMenu() {
        System.out.println("\n--- Операции ---");
        System.out.println("1. Создать операцию");
        System.out.println("2. Показать все операции");
        System.out.println("3. Операции по счёту");
        System.out.println("4. Изменить сумму");
        System.out.println("5. Изменить дату");
        System.out.println("6. Изменить описание");
        System.out.println("7. Удалить операцию");
        System.out.print("> ");

        switch (scanner.nextLine().trim()) {
            case "1" -> {
                System.out.print("id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Тип (INCOME/EXPENSE): ");
                String type = scanner.nextLine().trim();
                System.out.print("id счёта: ");
                String accountId = scanner.nextLine().trim();
                System.out.print("Сумма: ");
                String amount = scanner.nextLine().trim();
                System.out.print("Дата (yyyy-MM-dd): ");
                String date = scanner.nextLine().trim();
                System.out.print("Описание (Enter — пропустить): ");
                String desc = scanner.nextLine().trim();
                System.out.print("id категории: ");
                String categoryId = scanner.nextLine().trim();
                try {
                    if (desc.isBlank()) {
                        operationService.create(id, OperationType.valueOf(type), accountId,
                                new BigDecimal(amount), LocalDate.parse(date), categoryId);
                    } else {
                        operationService.create(id, OperationType.valueOf(type), accountId,
                                new BigDecimal(amount), LocalDate.parse(date), desc, categoryId);
                    }
                    System.out.println("Операция создана");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "2" -> operationService.findAll()
                    .forEach(o -> System.out.println(o.getId() + " | " + o.getType() + " | "
                            + o.getBankAccountId() + " | " + o.getAmount() + " | " + o.getDate()));
            case "3" -> {
                System.out.print("id счёта: ");
                operationService.findByBankAccountId(scanner.nextLine().trim())
                        .forEach(o -> System.out.println(o.getId() + " | " + o.getType() + " | "
                                + o.getAmount() + " | " + o.getDate()));
            }
            case "4" -> {
                System.out.print("id операции: ");
                String id = scanner.nextLine().trim();
                System.out.print("Новая сумма: ");
                try {
                    operationService.updateAmount(id, new BigDecimal(scanner.nextLine().trim()));
                    System.out.println("Обновлено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "5" -> {
                System.out.print("id операции: ");
                String id = scanner.nextLine().trim();
                System.out.print("Новая дата (yyyy-MM-dd): ");
                try {
                    operationService.updateDate(id, LocalDate.parse(scanner.nextLine().trim()));
                    System.out.println("Обновлено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "6" -> {
                System.out.print("id операции: ");
                String id = scanner.nextLine().trim();
                System.out.print("Новое описание: ");
                try {
                    operationService.updateDescription(id, scanner.nextLine().trim());
                    System.out.println("Обновлено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "7" -> {
                System.out.print("id операции: ");
                try {
                    operationService.delete(scanner.nextLine().trim());
                    System.out.println("Удалено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            default -> System.out.println("Неизвестная команда");
        }
    }

    private static void analyticsMenu() {
        System.out.println("\n--- Аналитика ---");
        System.out.println("1. Разница доходов и расходов за период");
        System.out.println("2. Группировка по категориям за период");
        System.out.print("> ");

        switch (scanner.nextLine().trim()) {
            case "1" -> {
                System.out.print("id счёта: ");
                String accountId = scanner.nextLine().trim();
                System.out.print("Дата от (yyyy-MM-dd): ");
                LocalDate from = LocalDate.parse(scanner.nextLine().trim());
                System.out.print("Дата до (yyyy-MM-dd): ");
                LocalDate to = LocalDate.parse(scanner.nextLine().trim());
                try {
                    BigDecimal diff = analyticalService.calculateDifference(accountId, from, to);
                    System.out.println("Результат: " + diff);
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "2" -> {
                System.out.print("id счёта: ");
                String accountId = scanner.nextLine().trim();
                System.out.print("Дата от (yyyy-MM-dd): ");
                LocalDate from = LocalDate.parse(scanner.nextLine().trim());
                System.out.print("Дата до (yyyy-MM-dd): ");
                LocalDate to = LocalDate.parse(scanner.nextLine().trim());
                try {
                    analyticalService.groupByCategory(accountId, from, to)
                            .forEach((c, amount) ->
                                    System.out.println(c.getName() + " (" + c.getType() + "): " + amount));
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            default -> System.out.println("Неизвестная команда");
        }
    }

    private static void exportMenu() {
        System.out.println("\n--- Экспорт / Импорт ---");
        System.out.println("1. Экспорт в JSON");
        System.out.println("2. Импорт из JSON");
        System.out.println("3. Экспорт в CSV");
        System.out.println("4. Импорт из CSV");
        System.out.println("5. Экспорт в YAML");
        System.out.println("6. Импорт из YAML");
        System.out.print("> ");

        switch (scanner.nextLine().trim()) {
            case "1" -> runExport(jsonExporter, "data.json");
            case "2" -> runImport(jsonImporter, "data.json");
            case "3" -> runExport(csvExporter, "data.csv");
            case "4" -> runImport(csvImporter, "data.csv");
            case "5" -> runExport(yamlExporter, "data.yaml");
            case "6" -> runImport(yamlImporter, "data.yaml");
            default -> System.out.println("Неизвестная команда");
        }
    }

    private static void runExport(DataExporter exporter, String defaultFile) {
        System.out.print("Путь к файлу (Enter — " + defaultFile + "): ");
        String path = scanner.nextLine().trim();
        if (path.isBlank()) path = defaultFile;
        try {
            ExportData data = new ExportData(
                    bankAccountService.findAll(),
                    categoryService.findAll(),
                    operationService.findAll()
            );
            exporter.export(data, path);
            System.out.println("Экспортировано в " + path);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void runImport(DataImporter importer, String defaultFile) {
        System.out.print("Путь к файлу (Enter — " + defaultFile + "): ");
        String path = scanner.nextLine().trim();
        if (path.isBlank()) path = defaultFile;
        try {
            ExportData data = importer.importData(path);
            data.getAccounts().forEach(a -> bankAccountService.create(a.getId(), a.getName(), a.getBalance()));
            data.getCategories().forEach(c -> categoryService.create(c.getId(), c.getType(), c.getName()));
            data.getOperations().forEach(o -> {
                if (o.getDescription() != null) {
                    operationService.create(o.getId(), o.getType(), o.getBankAccountId(),
                            o.getAmount(), o.getDate(), o.getDescription(), o.getCategoryId());
                } else {
                    operationService.create(o.getId(), o.getType(), o.getBankAccountId(),
                            o.getAmount(), o.getDate(), o.getCategoryId());
                }
            });
            System.out.println("Импортировано из " + path);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
