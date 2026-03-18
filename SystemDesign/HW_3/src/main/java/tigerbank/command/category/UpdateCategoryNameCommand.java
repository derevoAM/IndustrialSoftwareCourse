package tigerbank.command.category;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.CategoryFacade;

@RequiredArgsConstructor
public class UpdateCategoryNameCommand implements Command {
    private final CategoryFacade facade;
    private final String id;
    private final String newName;

    @Override
    public void execute() {
        facade.updateName(id, newName);
    }
}
