package tigerbank;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tigerbank.command.CommandExecutor;
import tigerbank.command.account.*;
import tigerbank.command.analytics.*;
import tigerbank.command.category.*;
import tigerbank.command.export.*;
import tigerbank.command.operation.*;
import tigerbank.domain.OperationType;
import tigerbank.export.*;
import tigerbank.facade.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static CommandExecutor executor;
    private static BankAccountFacade bankAccountFacade;
    private static CategoryFacade categoryFacade;
    private static OperationFacade operationFacade;
    private static AnalyticsFacade analyticsFacade;
    private static JsonExporter jsonExporter;
    private static JsonImporter jsonImporter;
    private static CsvExporter csvExporter;
    private static CsvImporter csvImporter;
    private static YamlExporter yamlExporter;
    private static YamlImporter yamlImporter;

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("tigerbank");
        executor = context.getBean(CommandExecutor.class);
        bankAccountFacade = context.getBean(BankAccountFacade.class);
        categoryFacade = context.getBean(CategoryFacade.class);
        operationFacade = context.getBean(OperationFacade.class);
        analyticsFacade = context.getBean(AnalyticsFacade.class);
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

            switch (scanner.nextLine().trim()) {
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
                try {
                    executor.execute(new CreateAccountCommand(bankAccountFacade, id, name));
                    System.out.println("Счёт создан");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "2" -> executor.execute(new FindAllAccountsCommand(bankAccountFacade));
            case "3" -> {
                System.out.print("id: ");
                executor.execute(new FindAccountByIdCommand(bankAccountFacade, scanner.nextLine().trim()));
            }
            case "4" -> {
                System.out.print("id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Новое название: ");
                String name = scanner.nextLine().trim();
                try {
                    executor.execute(new UpdateAccountNameCommand(bankAccountFacade, id, name));
                    System.out.println("Обновлено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "5" -> {
                System.out.print("id: ");
                try {
                    executor.execute(new DeleteAccountCommand(bankAccountFacade, scanner.nextLine().trim()));
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
                    executor.execute(new CreateCategoryCommand(categoryFacade, id, OperationType.valueOf(type), name));
                    System.out.println("Категория создана");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "2" -> executor.execute(new FindAllCategoriesCommand(categoryFacade));
            case "3" -> {
                System.out.print("id: ");
                executor.execute(new FindCategoryByIdCommand(categoryFacade, scanner.nextLine().trim()));
            }
            case "4" -> {
                System.out.print("id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Новое название: ");
                String name = scanner.nextLine().trim();
                try {
                    executor.execute(new UpdateCategoryNameCommand(categoryFacade, id, name));
                    System.out.println("Обновлено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "5" -> {
                System.out.print("id: ");
                try {
                    executor.execute(new DeleteCategoryCommand(categoryFacade, scanner.nextLine().trim()));
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
                    executor.execute(new CreateOperationCommand(operationFacade, id,
                            OperationType.valueOf(type), accountId,
                            new BigDecimal(amount), LocalDate.parse(date), desc, categoryId));
                    System.out.println("Операция создана");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "2" -> executor.execute(new FindAllOperationsCommand(operationFacade));
            case "3" -> {
                System.out.print("id счёта: ");
                executor.execute(new FindOperationsByAccountCommand(operationFacade, scanner.nextLine().trim()));
            }
            case "4" -> {
                System.out.print("id операции: ");
                String id = scanner.nextLine().trim();
                System.out.print("Новая сумма: ");
                try {
                    executor.execute(new UpdateOperationAmountCommand(operationFacade, id,
                            new BigDecimal(scanner.nextLine().trim())));
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
                    executor.execute(new UpdateOperationDateCommand(operationFacade, id,
                            LocalDate.parse(scanner.nextLine().trim())));
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
                    executor.execute(new UpdateOperationDescriptionCommand(operationFacade, id,
                            scanner.nextLine().trim()));
                    System.out.println("Обновлено");
                } catch (Exception e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }
            case "7" -> {
                System.out.print("id операции: ");
                try {
                    executor.execute(new DeleteOperationCommand(operationFacade, scanner.nextLine().trim()));
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
                    executor.execute(new CalculateDifferenceCommand(analyticsFacade, accountId, from, to));
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
                    executor.execute(new GroupByCategoryCommand(analyticsFacade, accountId, from, to));
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
            executor.execute(new ExportCommand(exporter, path,
                    bankAccountFacade, categoryFacade, operationFacade));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void runImport(DataImporter importer, String defaultFile) {
        System.out.print("Путь к файлу (Enter — " + defaultFile + "): ");
        String path = scanner.nextLine().trim();
        if (path.isBlank()) path = defaultFile;
        try {
            executor.execute(new ImportCommand(importer, path,
                    bankAccountFacade, categoryFacade, operationFacade));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
