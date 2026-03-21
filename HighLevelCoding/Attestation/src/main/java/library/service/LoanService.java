package library.service;

import library.domain.Book;
import library.domain.Loan;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {
    Loan lendBook(int bookId, int readerId, LocalDate dueDate);
    void returnBook(int bookId, int readerId);
    List<Book> getBorrowedByReader(int readerId);
}
