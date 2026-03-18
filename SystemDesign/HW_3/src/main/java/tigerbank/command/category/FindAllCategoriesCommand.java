package tigerbank.command.category;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.CategoryFacade;

@RequiredArgsConstructor
public class FindAllCategoriesCommand implements Command {
    private final CategoryFacade facade;

    @Override
    public void execute() {
        facade.findAll()
                .forEach(c -> System.out.println(c.getId() + " | " + c.getType() + " | " + c.getName()));
    }
}
