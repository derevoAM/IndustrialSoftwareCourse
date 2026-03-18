package tigerbank.command;

import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {

    public void execute(Command command) {
        long start = System.nanoTime();
        command.execute();
        long elapsed = System.nanoTime() - start;
        System.out.printf("[%.2f ms]%n", elapsed / 1_000_000.0);
    }
}
