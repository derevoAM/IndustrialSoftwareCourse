package library.service;

import library.db.DatabaseConnection;
import library.domain.Book;
import library.domain.Loan;
import library.domain.Reader;
import library.exception.BookNotAvailableException;
import library.exception.EntityNotFoundException;
import library.repository.BookRepository;
import library.repository.LoanRepository;
import library.repository.ReaderRepository;
import library.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock private LoanRepository loanRepository;
    @Mock private BookRepository bookRepository;
    @Mock private ReaderRepository readerRepository;
    @Mock private DatabaseConnection db;
    @Mock private Connection connection;

    private LoanServiceImpl loanService;

    private final Book book = new Book(1, "Чистый код", "Мартин", null, 2008, 2);
    private final Reader reader = new Reader(1, "Иванов", "ivanov@mail.ru", null, null);

    @BeforeEach
    void setUp() {
        loanService = new LoanServiceImpl(loanRepository, bookRepository, readerRepository, db);
    }

    @Test
    void givenAvailableBook_whenLendBook_thenLoanIsCreated() throws SQLException {
        when(db.getConnection()).thenReturn(connection);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1)).thenReturn(Optional.of(reader));
        when(bookRepository.countAvailableCopies(1)).thenReturn(2);
        when(loanRepository.findActiveByBookAndReader(1, 1)).thenReturn(Optional.empty());

        Loan result = loanService.lendBook(1, 1, LocalDate.now().plusDays(14));

        verify(loanRepository).save(any(Loan.class));
        verify(connection).commit();
        assertEquals(1, result.getBookId());
        assertEquals(1, result.getReaderId());
    }

    @Test
    void givenNoCopiesAvailable_whenLendBook_thenThrowsBookNotAvailableException() throws SQLException {
        when(db.getConnection()).thenReturn(connection);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1)).thenReturn(Optional.of(reader));
        when(bookRepository.countAvailableCopies(1)).thenReturn(0);

        assertThrows(BookNotAvailableException.class,
                () -> loanService.lendBook(1, 1, null));

        verify(loanRepository, never()).save(any());
    }

    @Test
    void givenReaderAlreadyHasBook_whenLendBook_thenThrowsBookNotAvailableException() throws SQLException {
        when(db.getConnection()).thenReturn(connection);
        Loan activeLoan = new Loan(1, 1, 1, null, null, null);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(readerRepository.findById(1)).thenReturn(Optional.of(reader));
        when(bookRepository.countAvailableCopies(1)).thenReturn(1);
        when(loanRepository.findActiveByBookAndReader(1, 1)).thenReturn(Optional.of(activeLoan));

        assertThrows(BookNotAvailableException.class,
                () -> loanService.lendBook(1, 1, null));

        verify(loanRepository, never()).save(any());
    }

    @Test
    void givenNonExistingBook_whenLendBook_thenThrowsEntityNotFoundException() {
        when(bookRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> loanService.lendBook(99, 1, null));

        verifyNoInteractions(loanRepository);
    }

    @Test
    void givenNonExistingReader_whenLendBook_thenThrowsEntityNotFoundException() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(readerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> loanService.lendBook(1, 99, null));

        verifyNoInteractions(loanRepository);
    }

    @Test
    void givenActiveLoan_whenReturnBook_thenLoanIsClosed() {
        when(loanRepository.closeActiveLoan(1, 1)).thenReturn(1);

        assertDoesNotThrow(() -> loanService.returnBook(1, 1));

        verify(loanRepository).closeActiveLoan(1, 1);
    }

    @Test
    void givenNoActiveLoan_whenReturnBook_thenThrowsEntityNotFoundException() {
        when(loanRepository.closeActiveLoan(1, 1)).thenReturn(0);

        assertThrows(EntityNotFoundException.class,
                () -> loanService.returnBook(1, 1));
    }

    @Test
    void givenReaderWithBooks_whenGetBorrowedByReader_thenReturnsBooksFromRepository() {
        when(readerRepository.findById(1)).thenReturn(Optional.of(reader));
        when(loanRepository.findActiveBooksForReader(1)).thenReturn(List.of(book));

        List<Book> result = loanService.getBorrowedByReader(1);

        assertEquals(1, result.size());
        assertEquals("Чистый код", result.get(0).getTitle());
    }

    @Test
    void givenNonExistingReader_whenGetBorrowedByReader_thenThrowsEntityNotFoundException() {
        when(readerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> loanService.getBorrowedByReader(99));

        verifyNoInteractions(loanRepository);
    }
}
