package library.service;

import library.domain.Reader;

import java.util.List;

public interface ReaderService {
    Reader register(String fullName, String email, String phone);
    List<Reader> listReaders();
    List<Reader> search(String query);
    Reader getById(int id);
}
