package tigerbank.command.category;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.CategoryFacade;

@RequiredArgsConstructor
public class FindCategoryByIdCommand implements Command {
    private final CategoryFacade facade;
    private final String id;

    @Override
    public void execute() {
        facade.findById(id).ifPresentOrElse(
                c -> System.out.println(c.getId() + " | " + c.getType() + " | " + c.getName()),
                () -> System.out.println("Не найдена"));
    }
}
