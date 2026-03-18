package tigerbank.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class CommandExecutorTest {

    private final CommandExecutor executor = new CommandExecutor();

    @Test
    @DisplayName("Executor вызывает execute() у переданной команды")
    void givenCommand_whenExecute_thenCommandExecuted() {
        AtomicBoolean executed = new AtomicBoolean(false);
        Command command = () -> executed.set(true);

        executor.execute(command);

        assertTrue(executed.get());
    }

    @Test
    @DisplayName("Executor выполняет несколько команд последовательно")
    void givenMultipleCommands_whenExecuteEach_thenAllCommandsExecuted() {
        AtomicBoolean first = new AtomicBoolean(false);
        AtomicBoolean second = new AtomicBoolean(false);

        executor.execute(() -> first.set(true));
        executor.execute(() -> second.set(true));

        assertTrue(first.get());
        assertTrue(second.get());
    }
}
