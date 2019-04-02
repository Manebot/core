package io.manebot.command.executor;

import io.manebot.command.CommandSender;
import io.manebot.command.exception.CommandExecutionException;

import java.util.List;

public class AliasedCommandExecutor implements CommandExecutor {
    private final CommandExecutor executor;
    private final String label;

    public AliasedCommandExecutor(CommandExecutor executor, String label) {
        this.executor = executor;
        this.label = label;
    }

    @Override
    public String getDescription() {
        return "Alias of \"" + label + "\": " + executor.getDescription();
    }

    @Override
    public List<String> getHelp(CommandSender sender, String label, String[] args) throws CommandExecutionException {
        return executor.getHelp(sender, label, args);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args)
            throws CommandExecutionException {
        executor.execute(sender, label, args);
    }

    @Override
    public boolean isBuffered() {
        return executor.isBuffered();
    }
}
