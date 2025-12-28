import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice;
            System.out.print("Выберите действие (1-10): ");
            if (!scanner.hasNextInt()) {
                scanner.next();
                choice = -1;
            } else {
                choice = scanner.nextInt();
                scanner.nextLine();
            }
            switch (choice) {
                case 1: {
                    System.out.print("Введите ФИО клиента: ");
                    String name = scanner.nextLine();
                    Customer customer = bank.createCustomer(name);
                    System.out.println("Создан клиент: " + name + "; id: " + customer.getId());
                    break;
                }
                case 2: {
                    System.out.print("Введите ID клиента: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Ошибка: введите число");
                        scanner.next();
                        break;
                    }
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Customer customer = bank.getCustomerById(id);
                    if (customer == null) {
                        System.out.println("Клиента с заданным id не существует");
                        break;
                    }
                    Account account = bank.openDebitAccount(bank.getCustomerById(id));
                    System.out.println("Открыт дебетовый счет: " + account.getAccountNumber() + "; id: " + id);
                    break;
                }
                case 3: {
                    System.out.print("Введите ID клиента: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Ошибка: введите число");
                        scanner.next();
                        break;
                    }
                    int id = scanner.nextInt();
                    System.out.print("Введите кредитный лимит: ");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Ошибка: введите число");
                        scanner.next();
                        break;
                    }
                    double limit = scanner.nextDouble();
                    scanner.nextLine();

                    Customer customer = bank.getCustomerById(id);
                    if (customer == null) {
                        System.out.println("Клиента с заданным id не существует");
                        break;
                    }

                    Account account = bank.openCreditAccount(bank.getCustomerById(id), limit);
                    System.out.println("Открыт кредитный счет: " + account.getAccountNumber() + "; id: " + id);
                    break;
                }
                case 4: {
                    System.out.print("Введите номер счета: ");
                    String accNum = scanner.nextLine();
                    System.out.print("Введите сумму для пополнения: ");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Ошибка: введите число");
                        scanner.next();
                        break;
                    }
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    boolean depResult = bank.deposit(accNum, amount);
                    String message = bank.getLatestTransaction().getMessage();
                    System.out.println(depResult ? "Пополнение успешно" : "Ошибка пополнения: " + message);
                    break;
                }
                case 5: {
                    System.out.print("Введите номер счета: ");
                    String accNum = scanner.nextLine();
                    System.out.print("Введите сумму для снятия: ");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Ошибка: введите число");
                        scanner.next();
                        break;
                    }
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    boolean withResult = bank.withdraw(accNum, amount);
                    String message = bank.getLatestTransaction().getMessage();
                    System.out.println(withResult ? "Снятие успешно" : "Ошибка снятия: " + message);
                    break;
                }
                case 6: {
                    System.out.print("Введите номер счета отправителя: ");
                    String fromAcc = scanner.nextLine();
                    System.out.print("Введите номер счета получателя: ");
                    String toAcc = scanner.nextLine();
                    System.out.print("Введите сумму перевода: ");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Ошибка: введите число");
                        scanner.next();
                        break;
                    }
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine();
                    boolean transResult = bank.transfer(fromAcc, toAcc, transferAmount);
                    String message = bank.getLatestTransaction().getMessage();
                    System.out.println(transResult ? "Перевод успешен" : "Ошибка перевода: " + message);
                    break;
                }
                case 7: {
                    System.out.print("Введите ID клиента: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Ошибка: введите число");
                        scanner.next();
                        break;
                    }
                    int customerId = scanner.nextInt();
                    scanner.nextLine();
                    bank.printCustomerAccounts(customerId);
                    break;
                }
                case 8:
                    bank.printTransactions();
                    break;

                case 9:
                    bank.printReport();
                    break;

                case 10:
                    System.out.println("Выход из программы...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неверный выбор! Введите число от 1 до 10.");
            }

        }
    }

    private static void printMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║        БАНКОВСКАЯ СИСТЕМА           ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ 1. Создать клиента                  ║");
        System.out.println("║ 2. Открыть дебетовый счет           ║");
        System.out.println("║ 3. Открыть кредитный счет           ║");
        System.out.println("║ 4. Пополнить счет                   ║");
        System.out.println("║ 5. Снять со счета                   ║");
        System.out.println("║ 6. Перевести между счетами          ║");
        System.out.println("║ 7. Показать счета клиента           ║");
        System.out.println("║ 8. Показать транзакции              ║");
        System.out.println("║ 9. Отчет банка                      ║");
        System.out.println("║ 10. Выход                           ║");
        System.out.println("╚══════════════════════════════════════╝");
    }
}