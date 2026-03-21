package library.service.impl;

import library.domain.Reader;
import library.exception.EntityNotFoundException;
import library.repository.ReaderRepository;
import library.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    @Override
    public Reader register(String fullName, String email, String phone) {
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("ФИО не может быть пустым");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email не может быть пустым");

        readerRepository.findByEmail(email.trim()).ifPresent(r -> {
            throw new IllegalArgumentException("Читатель с email '" + email + "' уже зарегистрирован");
        });

        Reader reader = new Reader(0, fullName.trim(), email.trim(),
                phone == null || phone.isBlank() ? null : phone.trim(), null);
        readerRepository.save(reader);
        return reader;
    }

    @Override
    public List<Reader> listReaders() {
        return readerRepository.findAll();
    }

    @Override
    public List<Reader> search(String query) {
        if (query == null || query.isBlank()) throw new IllegalArgumentException("Поисковый запрос не может быть пустым");
        return readerRepository.findByNameOrEmail(query.trim());
    }

    @Override
    public Reader getById(int id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Читатель не найден: id=" + id));
    }
}
