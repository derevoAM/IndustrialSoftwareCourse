package tigerbank.command.category;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.domain.OperationType;
import tigerbank.facade.CategoryFacade;

@RequiredArgsConstructor
public class CreateCategoryCommand implements Command {
    private final CategoryFacade facade;
    private final String id;
    private final OperationType type;
    private final String name;

    @Override
    public void execute() {
        facade.create(id, type, name);
    }
}
