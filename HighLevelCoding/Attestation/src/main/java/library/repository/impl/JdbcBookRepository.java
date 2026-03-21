package library.repository.impl;

import library.db.DatabaseConnection;
import library.domain.Book;
import library.repository.AbstractJdbcRepository;
import org.springframework.stereotype.Repository;
import library.repository.BookRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookRepository extends AbstractJdbcRepository implements BookRepository {

    public JdbcBookRepository(DatabaseConnection db) {
        super(db);
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, published_year, total_copies) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getPublishedYear());
            ps.setInt(5, book.getTotalCopies());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    book.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить книгу", e);
        }
    }

    @Override
    public Optional<Book> findById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти книгу по id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books ORDER BY title";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить список книг", e);
        }
        return books;
    }

    @Override
    public List<Book> findByTitle(String titlePattern) {
        String sql = "SELECT * FROM books WHERE title ILIKE ?";
        return findByPattern(sql, "%" + titlePattern + "%");
    }

    @Override
    public List<Book> findByAuthor(String authorPattern) {
        String sql = "SELECT * FROM books WHERE author ILIKE ?";
        return findByPattern(sql, "%" + authorPattern + "%");
    }

    @Override
    public List<Book> findByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        return findByPattern(sql, isbn);
    }

    @Override
    public int countAvailableCopies(int bookId) {
        String sql = """
                SELECT b.total_copies - COUNT(l.id) AS available
                FROM books b
                LEFT JOIN loans l ON l.book_id = b.id AND l.returned_at IS NULL
                WHERE b.id = ?
                GROUP BY b.total_copies
                """;
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось подсчитать доступные экземпляры", e);
        }
        return 0;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить книгу", e);
        }
    }

    private List<Book> findByPattern(String sql, String pattern) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось выполнить поиск книг", e);
        }
        return books;
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getInt("published_year"),
                rs.getInt("total_copies")
        );
    }
}
