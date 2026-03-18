package tigerbank.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class YamlExporter implements DataExporter {

    private final ObjectMapper mapper;

    public YamlExporter() {
        YAMLFactory factory = YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();
        this.mapper = new ObjectMapper(factory)
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void export(ExportData data, String filePath) {
        try {
            mapper.writeValue(new File(filePath), data);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при экспорте в YAML: " + filePath, e);
        }
    }
}
