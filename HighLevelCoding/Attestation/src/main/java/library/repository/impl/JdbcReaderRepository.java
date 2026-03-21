package library.repository.impl;

import library.db.DatabaseConnection;
import library.domain.Reader;
import library.repository.AbstractJdbcRepository;
import org.springframework.stereotype.Repository;
import library.repository.ReaderRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReaderRepository extends AbstractJdbcRepository implements ReaderRepository {

    public JdbcReaderRepository(DatabaseConnection db) {
        super(db);
    }

    @Override
    public void save(Reader reader) {
        String sql = "INSERT INTO readers (full_name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reader.getFullName());
            ps.setString(2, reader.getEmail());
            ps.setString(3, reader.getPhone());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    reader.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить читателя", e);
        }
    }

    @Override
    public Optional<Reader> findById(int id) {
        String sql = "SELECT * FROM readers WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти читателя по id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Reader> findByEmail(String email) {
        String sql = "SELECT * FROM readers WHERE email = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти читателя по email", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Reader> findByNameOrEmail(String query) {
        String sql = "SELECT * FROM readers WHERE full_name ILIKE ? OR email ILIKE ? ORDER BY full_name";
        String pattern = "%" + query + "%";
        List<Reader> readers = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    readers.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось найти читателей", e);
        }
        return readers;
    }

    @Override
    public List<Reader> findAll() {
        String sql = "SELECT * FROM readers ORDER BY full_name";
        List<Reader> readers = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                readers.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить список читателей", e);
        }
        return readers;
    }

    private Reader mapRow(ResultSet rs) throws SQLException {
        return new Reader(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getTimestamp("registered_at").toLocalDateTime()
        );
    }
}
