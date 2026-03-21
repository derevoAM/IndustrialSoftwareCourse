package library.service;

import library.domain.Book;
import library.search.BookSearchStrategy;

import java.util.List;

public interface BookService {
    Book addBook(String title, String author, String isbn, int publishedYear, int totalCopies);
    List<Book> listBooks();
    List<Book> search(String query, BookSearchStrategy strategy);
    Book getById(int id);
}
