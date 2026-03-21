package library.service.impl;

import library.domain.Book;
import library.exception.EntityNotFoundException;
import library.repository.BookRepository;
import library.search.BookSearchStrategy;
import library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book addBook(String title, String author, String isbn, int publishedYear, int totalCopies) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Название не может быть пустым");
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Автор не может быть пустым");
        if (totalCopies < 1) throw new IllegalArgumentException("Количество экземпляров должно быть не менее 1");

        Book book = new Book(0, title.trim(), author.trim(),
                isbn == null || isbn.isBlank() ? null : isbn.trim(),
                publishedYear, totalCopies);
        bookRepository.save(book);
        return book;
    }

    @Override
    public List<Book> listBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> search(String query, BookSearchStrategy strategy) {
        if (query == null || query.isBlank()) throw new IllegalArgumentException("Поисковый запрос не может быть пустым");
        return strategy.search(query.trim());
    }

    @Override
    public Book getById(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга не найдена: id=" + id));
    }
}
