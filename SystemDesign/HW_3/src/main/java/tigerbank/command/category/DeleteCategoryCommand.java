package tigerbank.command.category;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.CategoryFacade;

@RequiredArgsConstructor
public class DeleteCategoryCommand implements Command {
    private final CategoryFacade facade;
    private final String id;

    @Override
    public void execute() {
        facade.delete(id);
    }
}
