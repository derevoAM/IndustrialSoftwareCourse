package tigerbank.command.export;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.export.DataExporter;
import tigerbank.export.ExportData;
import tigerbank.facade.BankAccountFacade;
import tigerbank.facade.CategoryFacade;
import tigerbank.facade.OperationFacade;

@RequiredArgsConstructor
public class ExportCommand implements Command {
    private final DataExporter exporter;
    private final String filePath;
    private final BankAccountFacade bankAccountFacade;
    private final CategoryFacade categoryFacade;
    private final OperationFacade operationFacade;

    @Override
    public void execute() {
        ExportData data = new ExportData(
                bankAccountFacade.findAll(),
                categoryFacade.findAll(),
                operationFacade.findAll()
        );
        exporter.export(data, filePath);
        System.out.println("Экспортировано в " + filePath);
    }
}
