package library.repository;

import library.domain.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderRepository {
    void save(Reader reader);
    Optional<Reader> findById(int id);
    Optional<Reader> findByEmail(String email);
    List<Reader> findByNameOrEmail(String query);
    List<Reader> findAll();
}
