package library.repository;

import library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void save(Book book);
    Optional<Book> findById(int id);
    List<Book> findAll();
    List<Book> findByTitle(String titlePattern);
    List<Book> findByAuthor(String authorPattern);
    List<Book> findByIsbn(String isbn);
    int countAvailableCopies(int bookId);
    void deleteById(int id);
}
