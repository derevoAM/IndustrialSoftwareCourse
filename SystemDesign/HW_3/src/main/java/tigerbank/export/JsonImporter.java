package tigerbank.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonImporter extends AbstractImporter {

    private final ObjectMapper mapper;

    public JsonImporter() {
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Override
    protected ExportData parse(String content) {
        try {
            return mapper.readValue(content, ExportData.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при парсинге JSON", e);
        }
    }
}
