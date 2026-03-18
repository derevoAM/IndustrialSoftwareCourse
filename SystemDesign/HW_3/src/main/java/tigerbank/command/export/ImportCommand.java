package tigerbank.command.export;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.export.DataImporter;
import tigerbank.export.ExportData;
import tigerbank.facade.BankAccountFacade;
import tigerbank.facade.CategoryFacade;
import tigerbank.facade.OperationFacade;

@RequiredArgsConstructor
public class ImportCommand implements Command {
    private final DataImporter importer;
    private final String filePath;
    private final BankAccountFacade bankAccountFacade;
    private final CategoryFacade categoryFacade;
    private final OperationFacade operationFacade;

    @Override
    public void execute() {
        ExportData data = importer.importData(filePath);
        data.getAccounts().stream()
                .filter(a -> bankAccountFacade.findById(a.getId()).isEmpty())
                .forEach(a -> bankAccountFacade.create(a.getId(), a.getName()));
        data.getCategories().stream()
                .filter(c -> categoryFacade.findById(c.getId()).isEmpty())
                .forEach(c -> categoryFacade.create(c.getId(), c.getType(), c.getName()));
        data.getOperations().stream()
                .filter(o -> operationFacade.findById(o.getId()).isEmpty())
                .forEach(o -> {
                    if (o.getDescription() != null) {
                        operationFacade.create(o.getId(), o.getType(), o.getBankAccountId(),
                                o.getAmount(), o.getDate(), o.getDescription(), o.getCategoryId());
                    } else {
                        operationFacade.create(o.getId(), o.getType(), o.getBankAccountId(),
                                o.getAmount(), o.getDate(), o.getCategoryId());
                    }
                });
        System.out.println("Импортировано из " + filePath);
    }
}
