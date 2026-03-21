package library.service;

import library.domain.Reader;
import library.exception.EntityNotFoundException;
import library.repository.ReaderRepository;
import library.service.impl.ReaderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderServiceImplTest {

    @Mock
    private ReaderRepository readerRepository;

    private ReaderServiceImpl readerService;

    @BeforeEach
    void setUp() {
        readerService = new ReaderServiceImpl(readerRepository);
    }

    @Test
    void givenValidData_whenRegister_thenReaderIsSavedAndReturned() {
        when(readerRepository.findByEmail("ivanov@mail.ru")).thenReturn(Optional.empty());
        doAnswer(inv -> {
            ((Reader) inv.getArgument(0)).setId(1);
            return null;
        }).when(readerRepository).save(any(Reader.class));

        Reader result = readerService.register("Иванов Иван", "ivanov@mail.ru", "+7-900-000-00-00");

        verify(readerRepository).save(any(Reader.class));
        assertEquals("Иванов Иван", result.getFullName());
        assertEquals("ivanov@mail.ru", result.getEmail());
    }

    @Test
    void givenDuplicateEmail_whenRegister_thenThrowsIllegalArgumentException() {
        Reader existing = new Reader(1, "Существующий", "ivanov@mail.ru", null, null);
        when(readerRepository.findByEmail("ivanov@mail.ru")).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> readerService.register("Новый Читатель", "ivanov@mail.ru", null));

        verify(readerRepository, never()).save(any());
    }

    @Test
    void givenBlankName_whenRegister_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> readerService.register("  ", "email@mail.ru", null));

        verifyNoInteractions(readerRepository);
    }

    @Test
    void givenBlankEmail_whenRegister_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> readerService.register("Иванов", "", null));

        verifyNoInteractions(readerRepository);
    }

    @Test
    void givenExistingId_whenGetById_thenReturnsReader() {
        Reader reader = new Reader(1, "Иванов Иван", "ivanov@mail.ru", null, null);
        when(readerRepository.findById(1)).thenReturn(Optional.of(reader));

        Reader result = readerService.getById(1);

        assertEquals(1, result.getId());
        assertEquals("Иванов Иван", result.getFullName());
    }

    @Test
    void givenNonExistingId_whenGetById_thenThrowsEntityNotFoundException() {
        when(readerRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> readerService.getById(99));
    }
}
