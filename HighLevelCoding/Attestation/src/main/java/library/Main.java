package library;

import library.db.DatabaseConnection;
import library.domain.Book;
import library.domain.Reader;
import library.search.SearchByAuthor;
import library.search.SearchByIsbn;
import library.search.SearchByTitle;
import library.service.BookService;
import library.service.LoanService;
import library.service.ReaderService;
import library.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class Main implements CommandLineRunner {

    private final BookService bookService;
    private final ReaderService readerService;
    private final LoanService loanService;
    private final StatisticsService statisticsService;
    private final SearchByTitle searchByTitle;
    private final SearchByAuthor searchByAuthor;
    private final SearchByIsbn searchByIsbn;
    private final DatabaseConnection db;

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Библиотека ===");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> booksMenu();
                case "2" -> readersMenu();
                case "3" -> loansMenu();
                case "4" -> statisticsMenu();
                case "0" -> running = false;
                default -> System.out.println("Неизвестная команда. Попробуйте снова.");
            }
        }

        db.close();
        System.out.println("До свидания!");
    }

    private void printMainMenu() {
        System.out.println("\n--- Главное меню ---");
        System.out.println("1. Книги");
        System.out.println("2. Читатели");
        System.out.println("3. Выдача книг");
        System.out.println("4. Статистика");
        System.out.println("0. Выход");
        System.out.print("Выбор: ");
    }

    private void booksMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Книги ---");
            System.out.println("1. Добавить книгу");
            System.out.println("2. Список всех книг");
            System.out.println("3. Найти по названию");
            System.out.println("4. Найти по автору");
            System.out.println("5. Найти по ISBN");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> addBook();
                case "2" -> listBooks();
                case "3" -> findByTitle();
                case "4" -> findByAuthor();
                case "5" -> findByIsbn();
                case "0" -> back = true;
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void addBook() {
        try {
            System.out.print("Название: ");
            String title = scanner.nextLine().trim();
            System.out.print("Автор: ");
            String author = scanner.nextLine().trim();
            System.out.print("ISBN (оставьте пустым для пропуска): ");
            String isbn = scanner.nextLine().trim();
            System.out.print("Год издания (0 для пропуска): ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Количество экземпляров: ");
            int copies = Integer.parseInt(scanner.nextLine().trim());

            Book book = bookService.addBook(title, author, isbn.isEmpty() ? null : isbn, year, copies);
            System.out.println("Книга добавлена, id=" + book.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void listBooks() {
        List<Book> books = bookService.listBooks();
        if (books.isEmpty()) {
            System.out.println("Книги не найдены.");
            return;
        }
        System.out.printf("%-5s %-40s %-25s %-15s %-6s%n", "ID", "Название", "Автор", "ISBN", "Экз.");
        System.out.println("-".repeat(95));
        String table = books.stream()
                .map(b -> String.format("%-5d %-40s %-25s %-15s %-6d",
                        b.getId(), b.getTitle(), b.getAuthor(),
                        b.getIsbn() != null ? b.getIsbn() : "-", b.getTotalCopies()))
                .collect(Collectors.joining("\n"));
        System.out.println(table);
    }

    private void findByTitle() {
        System.out.print("Название: ");
        String query = scanner.nextLine().trim();
        printBookList(bookService.search(query, searchByTitle));
    }

    private void findByAuthor() {
        System.out.print("Автор: ");
        String query = scanner.nextLine().trim();
        printBookList(bookService.search(query, searchByAuthor));
    }

    private void findByIsbn() {
        System.out.print("ISBN: ");
        String query = scanner.nextLine().trim();
        printBookList(bookService.search(query, searchByIsbn));
    }

    private void printBookList(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("Книги не найдены.");
            return;
        }
        books.stream()
                .map(b -> String.format("[%d] %s — %s", b.getId(), b.getTitle(), b.getAuthor()))
                .forEach(System.out::println);
    }

    private void readersMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Читатели ---");
            System.out.println("1. Зарегистрировать читателя");
            System.out.println("2. Список всех читателей");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> registerReader();
                case "2" -> listReaders();
                case "0" -> back = true;
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void registerReader() {
        try {
            System.out.print("ФИО: ");
            String name = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Телефон (оставьте пустым для пропуска): ");
            String phone = scanner.nextLine().trim();

            Reader reader = readerService.register(name, email, phone.isEmpty() ? null : phone);
            System.out.println("Читатель зарегистрирован, id=" + reader.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void listReaders() {
        List<Reader> readers = readerService.listReaders();
        if (readers.isEmpty()) {
            System.out.println("Читатели не зарегистрированы.");
            return;
        }
        System.out.printf("%-5s %-30s %-30s %-15s%n", "ID", "ФИО", "Email", "Телефон");
        System.out.println("-".repeat(84));
        String table = readers.stream()
                .map(r -> String.format("%-5d %-30s %-30s %-15s",
                        r.getId(), r.getFullName(), r.getEmail(),
                        r.getPhone() != null ? r.getPhone() : "-"))
                .collect(Collectors.joining("\n"));
        System.out.println(table);
    }

    private void loansMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Выдача книг ---");
            System.out.println("1. Выдать книгу читателю");
            System.out.println("2. Вернуть книгу");
            System.out.println("3. Книги на руках у читателя");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> lendBook();
                case "2" -> returnBook();
                case "3" -> booksByReader();
                case "0" -> back = true;
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void lendBook() {
        try {
            Book book = pickBook();
            if (book == null) return;
            Reader reader = pickReader();
            if (reader == null) return;

            System.out.print("Срок возврата (ГГГГ-ММ-ДД, оставьте пустым для пропуска): ");
            String dueDateStr = scanner.nextLine().trim();
            LocalDate dueDate = dueDateStr.isEmpty() ? null : LocalDate.parse(dueDateStr);

            loanService.lendBook(book.getId(), reader.getId(), dueDate);
            System.out.println("Книга успешно выдана.");
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты. Используйте ГГГГ-ММ-ДД.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            Book book = pickBook();
            if (book == null) return;
            Reader reader = pickReader();
            if (reader == null) return;

            loanService.returnBook(book.getId(), reader.getId());
            System.out.println("Книга успешно возвращена.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void booksByReader() {
        try {
            Reader reader = pickReader();
            if (reader == null) return;

            List<Book> books = loanService.getBorrowedByReader(reader.getId());
            if (books.isEmpty()) {
                System.out.println("У этого читателя нет книг на руках.");
                return;
            }
            System.out.println("Книги на руках:");
            books.forEach(b -> System.out.printf("  [%d] %s — %s%n", b.getId(), b.getTitle(), b.getAuthor()));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private Book pickBook() {
        System.out.print("Название книги: ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("Запрос не может быть пустым.");
            return null;
        }
        List<Book> books = bookService.search(query, searchByTitle);
        if (books.isEmpty()) {
            System.out.println("Книги не найдены.");
            return null;
        }
        for (int i = 0; i < books.size(); i++) {
            Book b = books.get(i);
            System.out.printf("  %d. %s — %s%n", i + 1, b.getTitle(), b.getAuthor());
        }
        System.out.print("Выберите номер: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод.");
            return null;
        }
        if (idx < 0 || idx >= books.size()) {
            System.out.println("Неверный номер.");
            return null;
        }
        return books.get(idx);
    }

    private Reader pickReader() {
        System.out.print("Имя или email читателя: ");
        String query = scanner.nextLine().trim();
        if (query.isEmpty()) {
            System.out.println("Запрос не может быть пустым.");
            return null;
        }
        List<Reader> readers = readerService.search(query);
        if (readers.isEmpty()) {
            System.out.println("Читатели не найдены.");
            return null;
        }
        for (int i = 0; i < readers.size(); i++) {
            Reader r = readers.get(i);
            System.out.printf("  %d. %s — %s%n", i + 1, r.getFullName(), r.getEmail());
        }
        System.out.print("Выберите номер: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод.");
            return null;
        }
        if (idx < 0 || idx >= readers.size()) {
            System.out.println("Неверный номер.");
            return null;
        }
        return readers.get(idx);
    }

    private void statisticsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Статистика ---");
            System.out.println("1. Популярные книги (топ 10)");
            System.out.println("2. Все выданные книги");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> showPopularBooks();
                case "2" -> showAllIssuedBooks();
                case "0" -> back = true;
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void showPopularBooks() {
        List<String> rows = statisticsService.getPopularBooks();
        if (rows.isEmpty()) {
            System.out.println("Данных о выдачах пока нет.");
            return;
        }
        System.out.println("\nПопулярные книги:");
        rows.forEach(System.out::println);
    }

    private void showAllIssuedBooks() {
        List<String> rows = statisticsService.getAllIssuedBooks();
        if (rows.isEmpty()) {
            System.out.println("Сейчас нет выданных книг.");
            return;
        }
        System.out.println("\nВыданные книги:");
        rows.forEach(System.out::println);
    }
}
