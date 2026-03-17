package tigerbank.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsonImporter implements DataImporter {

    private final ObjectMapper mapper;

    public JsonImporter() {
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Override
    public ExportData importData(String filePath) {
        try {
            return mapper.readValue(new File(filePath), ExportData.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при импорте из JSON: " + filePath, e);
        }
    }
}
