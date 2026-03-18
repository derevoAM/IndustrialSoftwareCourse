package tigerbank.export;

public interface DataExporter {
    void export(ExportData data, String filePath);
}
