package tigerbank.export;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractImporter implements DataImporter {

    @Override
    public final ExportData importData(String filePath) {
        String content = readFile(filePath);
        return parse(content);
    }

    private String readFile(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
    }

    protected abstract ExportData parse(String content);
}
