package library.repository;

import library.domain.Book;
import library.domain.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    void save(Loan loan);
    Optional<Loan> findActiveByBookAndReader(int bookId, int readerId);
    List<Loan> findActiveByReaderId(int readerId);
    List<Book> findActiveBooksForReader(int readerId);
    List<Loan> findAllActive();
    int closeActiveLoan(int bookId, int readerId);
}
