package library.service.impl;

import library.db.DatabaseConnection;
import library.domain.Book;
import library.domain.Loan;
import library.exception.BookNotAvailableException;
import library.exception.EntityNotFoundException;
import library.repository.BookRepository;
import library.repository.LoanRepository;
import library.repository.ReaderRepository;
import library.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final DatabaseConnection db;

    @Override
    public Loan lendBook(int bookId, int readerId, LocalDate dueDate) {
        bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена: id=" + bookId));
        readerRepository.findById(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Читатель не найден: id=" + readerId));

        Connection con = null;
        try {
            con = db.getConnection();
            con.setAutoCommit(false);

            int available = bookRepository.countAvailableCopies(bookId);
            if (available <= 0) {
                throw new BookNotAvailableException("Нет доступных экземпляров книги id=" + bookId);
            }

            loanRepository.findActiveByBookAndReader(bookId, readerId).ifPresent(l -> {
                throw new BookNotAvailableException("Читатель уже взял эту книгу и не вернул её");
            });

            Loan loan = new Loan(0, bookId, readerId, null, dueDate, null);
            loanRepository.save(loan);

            con.commit();
            return loan;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { /* ignore */ }
            }
            throw new RuntimeException("Не удалось выдать книгу", e);
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
            }
        }
    }

    @Override
    public void returnBook(int bookId, int readerId) {
        int updated = loanRepository.closeActiveLoan(bookId, readerId);
        if (updated == 0) {
            throw new EntityNotFoundException(
                    "Активная выдача не найдена: bookId=" + bookId + ", readerId=" + readerId);
        }
    }

    @Override
    public List<Book> getBorrowedByReader(int readerId) {
        readerRepository.findById(readerId)
                .orElseThrow(() -> new EntityNotFoundException("Читатель не найден: id=" + readerId));

        return loanRepository.findActiveBooksForReader(readerId);
    }
}
