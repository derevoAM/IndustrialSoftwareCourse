package library.search;

import library.domain.Book;

import java.util.List;

@FunctionalInterface
public interface BookSearchStrategy {
    List<Book> search(String query);
}
