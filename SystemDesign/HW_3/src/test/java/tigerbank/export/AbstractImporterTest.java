package tigerbank.export;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class AbstractImporterTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Шаблонный метод читает файл и передаёт содержимое в parse()")
    void givenFileWithContent_whenImportData_thenParseReceivesFileContent() throws IOException {
        String expectedContent = "test content";
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, expectedContent);

        AtomicReference<String> receivedContent = new AtomicReference<>();
        AbstractImporter importer = new AbstractImporter() {
            @Override
            protected ExportData parse(String content) {
                receivedContent.set(content);
                return new ExportData(List.of(), List.of(), List.of());
            }
        };

        importer.importData(file.toString());

        assertEquals(expectedContent, receivedContent.get());
    }

    @Test
    @DisplayName("Шаблонный метод бросает исключение если файл не существует")
    void givenNonExistentFile_whenImportData_thenThrowsException() {
        AbstractImporter importer = new AbstractImporter() {
            @Override
            protected ExportData parse(String content) {
                return new ExportData(List.of(), List.of(), List.of());
            }
        };

        assertThrows(RuntimeException.class,
                () -> importer.importData("/nonexistent/path/file.txt"));
    }
}
