package tigerbank.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class YamlImporter extends AbstractImporter {

    private final ObjectMapper mapper;

    public YamlImporter() {
        this.mapper = new ObjectMapper(new YAMLFactory())
                .registerModule(new JavaTimeModule());
    }

    @Override
    protected ExportData parse(String content) {
        try {
            return mapper.readValue(content, ExportData.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при парсинге YAML", e);
        }
    }
}
