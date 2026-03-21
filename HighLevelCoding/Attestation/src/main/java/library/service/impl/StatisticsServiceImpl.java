package library.service.impl;

import library.db.DatabaseConnection;
import library.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final DatabaseConnection db;

    @Override
    public List<String> getPopularBooks() {
        String sql = """
                SELECT b.title, b.author, COUNT(l.id) AS total_loans
                FROM loans l
                JOIN books b ON b.id = l.book_id
                GROUP BY b.id, b.title, b.author
                ORDER BY total_loans DESC
                LIMIT 10
                """;
        List<String[]> rows = new ArrayList<>();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(new String[]{
                        rs.getString("title"),
                        rs.getString("author"),
                        String.valueOf(rs.getInt("total_loans"))
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить популярные книги", e);
        }

        AtomicInteger rank = new AtomicInteger(1);
        return rows.stream()
                .map(r -> String.format("%2d. %-40s %-30s выдач: %s",
                        rank.getAndIncrement(), r[0], r[1], r[2]))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllIssuedBooks() {
        String sql = """
                SELECT b.title, b.author, r.full_name, l.borrowed_at, l.due_date,
                       CASE WHEN l.due_date < CURRENT_DATE THEN 'ПРОСРОЧЕНО' ELSE 'ok' END AS status
                FROM loans l
                JOIN books b ON b.id = l.book_id
                JOIN readers r ON r.id = l.reader_id
                WHERE l.returned_at IS NULL
                ORDER BY l.due_date
                """;
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String dueDate = rs.getDate("due_date") != null
                        ? rs.getDate("due_date").toLocalDate().toString()
                        : "без срока";
                result.add(String.format("%-40s %-30s читатель: %-25s срок: %-12s [%s]",
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("full_name"),
                        dueDate,
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить список выданных книг", e);
        }
        return result;
    }
}
