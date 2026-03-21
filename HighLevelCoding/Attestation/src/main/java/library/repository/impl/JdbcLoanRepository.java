package library.repository.impl;

import library.db.DatabaseConnection;
import library.domain.Book;
import library.domain.Loan;
import library.repository.AbstractJdbcRepository;
import org.springframework.stereotype.Repository;
import library.repository.LoanRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcLoanRepository extends AbstractJdbcRepository implements LoanRepository {

    public JdbcLoanRepository(DatabaseConnection db) {
        super(db);
    }

    @Override
    public void save(Loan loan) {
        String sql = "INSERT INTO loans (book_id, reader_id, due_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, loan.getBookId());
            ps.setInt(2, loan.getReaderId());
            if (loan.getDueDate() != null) {
                ps.setDate(3, Date.valueOf(loan.getDueDate()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    loan.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить выдачу", e);
        }
    }

    @Override
    public Optional<Loan> findActiveByBookAndReader(int bookId, int readerId) {
        String sql = "SELECT * FROM loans WHERE book_id = ? AND reader_id = ? AND returned_at IS NULL";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти активную выдачу", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Loan> findActiveByReaderId(int readerId) {
        String sql = """
                SELECT l.id, l.book_id, l.reader_id, l.borrowed_at, l.due_date, l.returned_at
                FROM loans l
                WHERE l.reader_id = ? AND l.returned_at IS NULL
                ORDER BY l.borrowed_at DESC
                """;
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти выдачи по читателю", e);
        }
        return loans;
    }

    @Override
    public List<Book> findActiveBooksForReader(int readerId) {
        String sql = """
                SELECT b.id, b.title, b.author, b.isbn, b.published_year, b.total_copies
                FROM books b
                JOIN loans l ON l.book_id = b.id
                WHERE l.reader_id = ? AND l.returned_at IS NULL
                ORDER BY l.borrowed_at DESC
                """;
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getInt("published_year"),
                            rs.getInt("total_copies")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить книги читателя", e);
        }
        return books;
    }

    @Override
    public List<Loan> findAllActive() {
        String sql = """
                SELECT l.id, l.book_id, l.reader_id, l.borrowed_at, l.due_date, l.returned_at
                FROM loans l
                WHERE l.returned_at IS NULL
                ORDER BY l.due_date
                """;
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                loans.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить активные выдачи", e);
        }
        return loans;
    }

    @Override
    public int closeActiveLoan(int bookId, int readerId) {
        String sql = "UPDATE loans SET returned_at = NOW() WHERE book_id = ? AND reader_id = ? AND returned_at IS NULL";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, readerId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось закрыть выдачу", e);
        }
    }

    private Loan mapRow(ResultSet rs) throws SQLException {
        Date dueDateSql = rs.getDate("due_date");
        LocalDate dueDate = dueDateSql != null ? dueDateSql.toLocalDate() : null;

        Timestamp returnedAtTs = rs.getTimestamp("returned_at");

        return new Loan(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getInt("reader_id"),
                rs.getTimestamp("borrowed_at").toLocalDateTime(),
                dueDate,
                returnedAtTs != null ? returnedAtTs.toLocalDateTime() : null
        );
    }
}
