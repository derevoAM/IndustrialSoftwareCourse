package library.service;

import java.util.List;

public interface StatisticsService {
    List<String> getPopularBooks();
    List<String> getAllIssuedBooks();
}
