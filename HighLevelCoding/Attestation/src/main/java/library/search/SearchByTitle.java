package library.search;

import library.domain.Book;
import library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchByTitle implements BookSearchStrategy {

    private final BookRepository bookRepository;

    @Override
    public List<Book> search(String query) {
        return bookRepository.findByTitle(query);
    }
}
