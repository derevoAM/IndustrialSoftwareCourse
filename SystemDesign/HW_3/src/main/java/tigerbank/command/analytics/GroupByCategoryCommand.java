package tigerbank.command.analytics;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.AnalyticsFacade;

import java.time.LocalDate;

@RequiredArgsConstructor
public class GroupByCategoryCommand implements Command {
    private final AnalyticsFacade facade;
    private final String bankAccountId;
    private final LocalDate from;
    private final LocalDate to;

    @Override
    public void execute() {
        facade.groupByCategory(bankAccountId, from, to)
                .forEach((c, amount) ->
                        System.out.println(c.getName() + " (" + c.getType() + "): " + amount));
    }
}
