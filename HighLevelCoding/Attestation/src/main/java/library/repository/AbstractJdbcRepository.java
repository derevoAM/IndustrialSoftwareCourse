package library.repository;

import library.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractJdbcRepository {
    protected final DatabaseConnection db;
    protected AbstractJdbcRepository(DatabaseConnection db) {
        this.db = db;
    }
    protected Connection getConnection() throws SQLException {
        return db.getConnection();
    }
}
