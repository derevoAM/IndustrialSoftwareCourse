package tigerbank.command.analytics;

import lombok.RequiredArgsConstructor;
import tigerbank.command.Command;
import tigerbank.facade.AnalyticsFacade;

import java.time.LocalDate;

@RequiredArgsConstructor
public class CalculateDifferenceCommand implements Command {
    private final AnalyticsFacade facade;
    private final String bankAccountId;
    private final LocalDate from;
    private final LocalDate to;

    @Override
    public void execute() {
        System.out.println("Результат: " + facade.calculateDifference(bankAccountId, from, to));
    }
}
