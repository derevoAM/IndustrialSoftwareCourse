import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private ArrayList<Customer> customers;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactions;
    private Random random;

    public Bank() {
        customers = new ArrayList<Customer>();
        accounts = new ArrayList<Account>();
        transactions = new ArrayList<Transaction>();
        random = new Random();
    }


    public Customer createCustomer(String fullName) {
        int id = customers.size() + 1;
        Customer customer = new Customer(id, fullName);
        customers.add(customer);
        return customer;
    }

    public Account openDebitAccount(Customer owner) {

        Account account = new DebitAccount("DEB" + String.format("%07d", random.nextInt(10000000)), 0.0, owner);
        accounts.add(account);
        return account;
    }

    public Account openCreditAccount(Customer owner, double creditLimit) {
        Account account = new CreditAccount("CRE" + String.format("%07d", random.nextInt(10000000)), 0.0, owner, creditLimit);
        accounts.add(account);
        return account;
    }

    public boolean deposit(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        String message = "OK";
        boolean success = true;
        if (account == null) {
            message = "Аккаунт не сущестует";
            success = false;
        } else {
            success = account.deposit(amount);
            if (!success) message = "Сумма пополнения должна быть положительной";
        }
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount,
                null, accountNumber, success, message);
        transactions.add(transaction);
        return success;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        String message = "OK";
        boolean success = true;
        if (account == null) {
            message = "Аккаунт не сущестует";
            success = false;
        } else {
            success = account.withdraw(amount);
            if (!success) {
                if (amount <= 0) message = "Сумма пополнения должна быть положительной";
                else message = "Недостаточно средств";
            }
        }
        Transaction transaction = new Transaction(TransactionType.WITHDRAW, amount,
                accountNumber, null, success, message);
        transactions.add(transaction);
        return success;
    }

    public boolean transfer(String from, String to, double amount) {
        String message = "OK";
        boolean success = true;

        Account fromAcc = findAccount(from);
        Account toAcc = findAccount(to);

        if (fromAcc == null) {
            message = "Счет отправителя не существует";
            success = false;
        } else if (toAcc == null) {
            message = "Счет приема не существует";
            success = false;
        } else if (amount <= 0) {
            message = "Сумма перевода должна быть положительной";
            success = false;
        } else if (from.equals(to)) {
            message = "Нельзя переводить на тот же счет";
            success = false;
        } else {
            success = fromAcc.transfer(toAcc, amount);
            if (!success) {
                message = "Недостаточно средств";
            }
        }

        Transaction transaction = new Transaction(TransactionType.TRANSFER, amount,
                from, to, success, message);
        transactions.add(transaction);

        return success;
    }

    public Account findAccount(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }

    public void printCustomerAccounts(int customerId) {
        for (Account acc : accounts) {
            if (acc.getOwner().getId() == customerId) {
                System.out.println("Номер счета: " + acc.getAccountNumber() + "; баланс: " + acc.getBalance());
            }
        }
    }

    public void printTransactions() {
        System.out.println("=== ИСТОРИЯ ТРАНЗАКЦИЙ ===");

        if (transactions.isEmpty()) {
            System.out.println("Транзакций нет");
            return;
        }

        int counter = 1;
        for (Transaction t : transactions) {
            String time = t.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            String type = "";

            switch (t.getType()) {
                case DEPOSIT:
                    type = "ПОПОЛНЕНИЕ";
                    break;
                case WITHDRAW:
                    type = "СНЯТИЕ";
                    break;
                case TRANSFER:
                    type = "ПЕРЕВОД";
                    break;
            }

            String status = t.isSuccess() ? "УСПЕШНО" : "ОШИБКА";

            System.out.println(counter++ + ". [" + time + "] " + type);
            System.out.println("   Сумма: " + String.format("%.2f", t.getAmount()) + " руб.");

            if (t.getFromAccountNumber() != null) {
                System.out.println("   Счет отправителя: " + t.getFromAccountNumber());
            }
            if (t.getToAccountNumber() != null) {
                System.out.println("   Счет получателя: " + t.getToAccountNumber());
            }

            System.out.println("   Статус: " + status);

            if (!t.isSuccess()) {
                System.out.println("   Причина: " + t.getMessage());
            }

            System.out.println();
        }

        System.out.println("Всего транзакций: " + transactions.size());
    }

    public void printReport() {
        int debitCount = 0, creditCount = 0;
        int successCount = 0, failCount = 0;
        double totalDebitBalance = 0.0;
        double totalCreditBalance = 0.0;

        for (Account acc : accounts) {
            if (acc instanceof DebitAccount) {
                debitCount++;
                totalDebitBalance += acc.getBalance();
            } else if (acc instanceof CreditAccount) {
                creditCount++;
                totalCreditBalance += acc.getBalance();
            }
        }

        for (Transaction t : transactions) {
            if (t.isSuccess()) successCount++;
            else failCount++;
        }

        System.out.println("=== ОТЧЕТ БАНКА ===");
        System.out.println("1. Статистика счетов:");
        System.out.println("Дебетовых счетов: " + debitCount);
        System.out.println("Кредитных счетов: " + creditCount);
        System.out.println("Всего счетов: " + (debitCount + creditCount));

        System.out.println("\n2. Суммарные балансы:");
        System.out.println("Баланс дебетовых счетов: " + String.format("%.2f", totalDebitBalance) + " руб.");
        System.out.println("Баланс кредитных счетов: " + String.format("%.2f", totalCreditBalance) + " руб.");
        System.out.println("Общий баланс: " + String.format("%.2f", totalDebitBalance + totalCreditBalance) + " руб.");

        System.out.println("\n3. Статистика операций:");
        System.out.println("Успешных операций: " + successCount);
        System.out.println("Неуспешных операций: " + failCount);
        System.out.println("Всего операций: " + (successCount + failCount));
    }

    public Customer getCustomerById(int id) {
        if (1 <= id && id <= customers.size()) return customers.get(id - 1);
        else return null;
    }

    public Transaction getLatestTransaction() {
        return transactions.get(transactions.size() - 1);
    }
}

