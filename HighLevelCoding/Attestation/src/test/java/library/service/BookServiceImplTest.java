package library.service;

import library.domain.Book;
import library.exception.EntityNotFoundException;
import library.repository.BookRepository;
import library.search.BookSearchStrategy;
import library.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    void givenValidData_whenAddBook_thenBookIsSavedAndReturned() {
        Book book = new Book(1, "Чистый код", "Роберт Мартин", "978-0-13-468599-1", 2008, 3);
        doAnswer(inv -> {
            ((Book) inv.getArgument(0)).setId(1);
            return null;
        }).when(bookRepository).save(any(Book.class));

        Book result = bookService.addBook("Чистый код", "Роберт Мартин", "978-0-13-468599-1", 2008, 3);

        verify(bookRepository).save(any(Book.class));
        assertEquals("Чистый код", result.getTitle());
        assertEquals("Роберт Мартин", result.getAuthor());
    }

    @Test
    void givenBlankTitle_whenAddBook_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> bookService.addBook("  ", "Автор", null, 2000, 1));

        verifyNoInteractions(bookRepository);
    }

    @Test
    void givenBlankAuthor_whenAddBook_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> bookService.addBook("Название", "", null, 2000, 1));

        verifyNoInteractions(bookRepository);
    }

    @Test
    void givenZeroCopies_whenAddBook_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> bookService.addBook("Название", "Автор", null, 2000, 0));

        verifyNoInteractions(bookRepository);
    }

    @Test
    void givenExistingId_whenGetById_thenReturnsBook() {
        Book book = new Book(1, "Война и мир", "Лев Толстой", null, 1869, 2);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book result = bookService.getById(1);

        assertEquals(1, result.getId());
        assertEquals("Война и мир", result.getTitle());
    }

    @Test
    void givenNonExistingId_whenGetById_thenThrowsEntityNotFoundException() {
        when(bookRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getById(99));
    }

    @Test
    void givenQuery_whenSearch_thenDelegatesToStrategy() {
        Book book = new Book(1, "Мастер и Маргарита", "Булгаков", null, 1967, 3);
        BookSearchStrategy strategy = mock(BookSearchStrategy.class);
        when(strategy.search("Булгаков")).thenReturn(List.of(book));

        List<Book> result = bookService.search("Булгаков", strategy);

        verify(strategy).search("Булгаков");
        assertEquals(1, result.size());
        assertEquals("Мастер и Маргарита", result.get(0).getTitle());
    }

    @Test
    void givenBlankQuery_whenSearch_thenThrowsIllegalArgumentException() {
        BookSearchStrategy strategy = mock(BookSearchStrategy.class);

        assertThrows(IllegalArgumentException.class,
                () -> bookService.search("  ", strategy));

        verifyNoInteractions(strategy);
    }
}
